import json
import logging
import os
import shutil

import requests

# Configure logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

# Environment variable and file path constants
API_TOKEN_ENV = 'REPLICATE_API_TOKEN'
REPLICATE_MODELS_ENV = 'REPLICATE_MODELS'
DEFAULT_MODELS_FILE = 'default_replicate_models.txt'
OUTPUT_DIRECTORY = 'schemas'
ENUM_SCHEMA_FILENAME = 'ReplicateModels.json'

# Read API token from environment variable
API_TOKEN = os.getenv(API_TOKEN_ENV)
if not API_TOKEN:
    raise RuntimeError(f'Please set the {API_TOKEN_ENV} environment variable')

HEADERS = {
    'Authorization': f'Token {API_TOKEN}',
    'Accept': 'application/json',
}

# Clear the output directory if it already exists
if os.path.exists(OUTPUT_DIRECTORY):
    shutil.rmtree(OUTPUT_DIRECTORY)
os.makedirs(OUTPUT_DIRECTORY, exist_ok=True)

# Read model owners and names from environment variable or fallback to default file
models_env = os.getenv(REPLICATE_MODELS_ENV)
if models_env:
    models = models_env.split(',')
else:
    with open(DEFAULT_MODELS_FILE, 'r') as f:
        models = [line.strip() for line in f if line.strip()]


def fetch_model_schema(owner, name):
    url = f'https://api.replicate.com/v1/models/{owner}/{name}'
    response = requests.get(url, headers=HEADERS)
    if response.status_code != 200:
        logger.error(f'Failed to fetch model {owner}/{name}: {response.status_code}')
        return None
    return response.json().get('latest_version', {}).get('openapi_schema', {}).get('components', {}).get('schemas')


def adjust_refs(schema, schemas, schema_files):
    """ Recursively adjust $ref pointers in the schema """
    if isinstance(schema, dict):
        for key, value in schema.items():
            if key == '$ref' and value.startswith('#/components/schemas/'):
                ref_schema_name = value.split('/')[-1]
                schema['$ref'] = schema_files.get(ref_schema_name, value)
            else:
                adjust_refs(value, schemas, schema_files)
    elif isinstance(schema, list):
        for item in schema:
            adjust_refs(item, schemas, schema_files)


def save_schema(owner, name, schema_name, schema, schema_files, schemas):
    """ Save a schema to a JSON file and adjust references """
    logger.info(f"Processing schema: {schema_name}")

    # Generate a valid filename for the schema
    class_name = f"{owner}_{name}_{schema_name}"
    class_name = ''.join(e for e in class_name.title() if e.isalnum())
    filename = f'{class_name}.json'
    schema_files[schema_name] = filename

    # Adjust references within the schema
    adjust_refs(schema, schemas, schema_files)

    # Save the schema to a file
    filepath = os.path.join(OUTPUT_DIRECTORY, filename)
    with open(filepath, 'w') as outfile:
        json.dump(schema, outfile, indent=2)
    logger.info(f'Saved schema for {schema_name} to {filepath}')


def generate_model_enum_schema(models):
    """ Generate a JSON schema that represents an enum of all models """
    enum_entries = []
    for model in models:
        owner, name = model.split('/')
        constant_name = f"{owner}_{name}".upper().replace('-', '_')
        enum_entries.append(constant_name)

    enum_schema = {
        "$schema": "http://json-schema.org/draft-07/schema#",
        "title": "ReplicateModels",
        "type": "string",
        "enum": enum_entries
    }

    # Save the enum schema to a file
    filepath = os.path.join(OUTPUT_DIRECTORY, ENUM_SCHEMA_FILENAME)
    with open(filepath, 'w') as outfile:
        json.dump(enum_schema, outfile, indent=2)
    logger.info(f'Saved model enum schema to {filepath}')


def main():
    for model in models:
        owner, name = model.split('/')
        schemas = fetch_model_schema(owner, name)
        if not schemas:
            logger.warning(f'Schemas not found for model {model}')
            continue

        schema_files = {}
        for schema_name, schema in schemas.items():
            save_schema(owner, name, schema_name, schema, schema_files, schemas)

    # Generate the enum schema for all models
    generate_model_enum_schema(models)


if __name__ == "__main__":
    main()
