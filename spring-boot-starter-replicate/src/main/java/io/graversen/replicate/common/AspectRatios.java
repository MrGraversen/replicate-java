package io.graversen.replicate.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum AspectRatios {
    RATIO_1_BY_1(1, 1),
    RATIO_16_BY_9(16, 9),
    RATIO_21_BY_9(21, 9),
    RATIO_3_BY_2(3, 2),
    RATIO_2_BY_3(2, 3),
    RATIO_4_BY_5(4, 5),
    RATIO_5_BY_4(5, 4),
    RATIO_3_BY_4(3, 4),
    RATIO_4_BY_3(4, 3),
    RATIO_9_BY_16(9, 16),
    RATIO_9_BY_21(9, 21);

    private final int widthRatio;
    private final int heightRatio;

    public static Optional<AspectRatios> fromValue(String value) {
        return Arrays.stream(AspectRatios.values())
                .filter(ratio -> ratio.getAspectRatio().equals(value))
                .findFirst();
    }

    public String getAspectRatio() {
        return widthRatio + ":" + heightRatio;
    }
}
