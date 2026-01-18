# Replicate AI, powered by Java 21 ☕ & Spring Boot 3 🍃

Welcome to the [**Replicate AI**](https://replicate.com/) Integration project! 🎉

This project provides a streamlined Java 21-based framework to effortlessly interact with and create predictions for *
*Replicate**'s
powerful AI models. Built with **Spring Boot**, it enables quick and modular access to various AI services, making
integration a breeze for
developers.

The project allows downloading of **Replicate AI** models' JSONSchemas for requests and responses through the official
API, and dynamically
creates a Maven module to enable developers to interact with the AI models using strongly typed Java code.

Furthermore, it supports **Spring Boot AutoConfiguration**, allowing efficient and powerful integration with Spring Boot
applications to
enable easy access to **Replicate**'s AI offerings—just add your API key!

## 🚀 Quick Start

The quickest way to get off the ground is depending on `spring-boot-starter-replicate`. The module, provided an API key
to Replicate,
handles all configuration of the underlying modules to ensure a smooth start.

```xml

<dependency>
    <groupId>io.graversen</groupId>
    <artifactId>spring-boot-starter-replicate</artifactId>
    <version>0.0.8</version>
</dependency>
```

Add your Replicate API key to your `application.yml` file.

```yml
replicate:
  token: r8...
```

... And you're good to go!

## Replicate Models

By default, this project supports a number of common AI models for text completion using large language models and
text-to-image models for
image generation. Specifically, the following models are currently supported out of the box:

### Black Forest Labs Flux

* [`flux-2-dev`](https://replicate.com/black-forest-labs/flux-2-dev)
* [`flux-2-flex`](https://replicate.com/black-forest-labs/flux-2-flex)
* [`flux-2-pro`](https://replicate.com/black-forest-labs/flux-2-pro)
* [`flux-2-max`](https://replicate.com/black-forest-labs/flux-2-max)

---

> [!TIP]
> It is possible to build this project to support different AI models that are not included by default for simplicity
> reasons.
> Please see the section describing the `replicate-tools` module.

## 📂 Module Overview

Below is a more focused description regarding each discrete module of this project.

### `replicate-client`

Core module for handling API requests and responses from **Replicate**. Use this if you only want to create predictions
with Java code in
the simplest possible manner.

### `replicate-models`

Auto-generated models from **Replicate**’s JSONSchemas, providing strongly typed Java interfaces for AI models. The
models supported are
generated using `replicate-tools`. From this repository, the following packages are supported:

* [`default`](https://github.com/MrGraversen/replicate-java/packages/2297525?version=0.0.8-default): Default models as
  described above.
* [`openai`](https://github.com/MrGraversen/replicate-java/packages/2297525?version=0.0.8-openai): A specialized module
  consisting only of OpenAI models.
* [`flux`](https://github.com/MrGraversen/replicate-java/packages/2297525?version=0.0.8-flux): A specialized module
  consisting only of the
  Flux family of AI models.

### `replicate-tools`

Python project to interact with **Replicate** API services to get all JSONSchemas of supplied models.

### Example Usage

Install:

```shell
python -m pip install --upgrade pip
pip install -r replicate-tools/requirements.txt
```

Run Python script with your supplied `REPLICATE_MODELS`.

> [!TIP]
> The models follow a `owner` / `model-name` convention, just fetch it from the Replicate URL.
> For example: `https://replicate.com/black-forest-labs/flux-dev` → `black-forest-labs/flux-dev`

```shell
REPLICATE_API_TOKEN="r8..." REPLICATE_MODELS="meta/meta-llama-3-8b-instruct,black-forest-labs/flux-dev" python download_replicate_schemas.py
```

After this, you are able to build the `replicate-models` module using Maven.

```shell
mvn compile
```

> [!TIP]
> If you decide to use AI models that are more complex than simple text completion or text-to-image generators, note
> that you may depend on
> just `replicate-client` and `replicate-models` with your custom set of models, to enable easy, strongly typed access
> to the Replicate API.

### `spring-boot-starter-replicate`

Auto-configures **Replicate** integration, provided an API token. Through this, you will have access to more advanced
encapsulations to
create images and manage conversations.

## 📈 Examples

### Example – Image generation with Flux 2 Pro

```java

@Slf4j
@Component
@RequiredArgsConstructor
public class Flux2Example {
    private final ReplicateFacade replicateFacade;

    /**
     * Create one image using flux2-dev
     */
    public void runExample() {
        final var createImagePrediction = CreateImagePrediction2.builder()
                .prompt("A photo of a happy sheep grazing on a beautiful summer day")
                .aspectRatio(AspectRatios.RATIO_5_BY_4)
                .build();

        replicateFacade.createPrediction(FluxModels.FLUX_2_PRO, createImagePrediction);
    }

    @EventListener(value = PredictionUpdatedEvent.class, condition = "#event.getStatus().name() == 'SUCCEEDED'")
    public void onPredictionUpdated(PredictionUpdatedEvent event) {
        log.info(
                "Completed prediction '{}' with model '{}', view it here: {}",
                event.getId(),
                event.getModel(),
                ReplicateUrl.fromPrediction(event.getId())
        );
    }
}
```

---

> [!NOTE]  
> This project is still in the early stages of development.
