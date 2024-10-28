package io.graversen.replicate.common;

import lombok.NonNull;
import lombok.Value;

@Value
public class ReplicateModel {
    @NonNull String owner;
    @NonNull String name;
}
