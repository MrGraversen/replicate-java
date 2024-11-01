package io.graversen.replicate.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextToImageAspectRatioTest {
    @Test
    public void testAspectRatio_1By1() {
        TextToImageAspectRatio aspectRatio = new TextToImageAspectRatio(100, AspectRatios.RATIO_1_BY_1);
        assertEquals(100, aspectRatio.getWidth());
        assertEquals(100, aspectRatio.getHeight());
    }

    @Test
    public void testAspectRatio_16By9() {
        TextToImageAspectRatio aspectRatio = new TextToImageAspectRatio(180, AspectRatios.RATIO_16_BY_9);
        assertEquals(320, aspectRatio.getWidth());
        assertEquals(180, aspectRatio.getHeight());
    }

    @Test
    public void testAspectRatio_21By9() {
        TextToImageAspectRatio aspectRatio = new TextToImageAspectRatio(180, AspectRatios.RATIO_21_BY_9);
        assertEquals(420, aspectRatio.getWidth());
        assertEquals(180, aspectRatio.getHeight());
    }

    @Test
    public void testAspectRatio_3By2() {
        TextToImageAspectRatio aspectRatio = new TextToImageAspectRatio(200, AspectRatios.RATIO_3_BY_2);
        assertEquals(300, aspectRatio.getWidth());
        assertEquals(200, aspectRatio.getHeight());
    }

    @Test
    public void testAspectRatio_2By3() {
        TextToImageAspectRatio aspectRatio = new TextToImageAspectRatio(300, AspectRatios.RATIO_2_BY_3);
        assertEquals(200, aspectRatio.getWidth());
        assertEquals(300, aspectRatio.getHeight());
    }

    @Test
    public void testAspectRatio_4By5() {
        TextToImageAspectRatio aspectRatio = new TextToImageAspectRatio(1350, AspectRatios.RATIO_4_BY_5);
        assertEquals(1080, aspectRatio.getWidth());
        assertEquals(1350, aspectRatio.getHeight());
    }

    @Test
    public void testAspectRatio_5By4() {
        TextToImageAspectRatio aspectRatio = new TextToImageAspectRatio(1080, AspectRatios.RATIO_5_BY_4);
        assertEquals(1350, aspectRatio.getWidth());
        assertEquals(1080, aspectRatio.getHeight());
    }

    @Test
    public void testAspectRatio_3By4() {
        TextToImageAspectRatio aspectRatio = new TextToImageAspectRatio(300, AspectRatios.RATIO_3_BY_4);
        assertEquals(225, aspectRatio.getWidth());
        assertEquals(300, aspectRatio.getHeight());
    }

    @Test
    public void testAspectRatio_4By3() {
        TextToImageAspectRatio aspectRatio = new TextToImageAspectRatio(120, AspectRatios.RATIO_4_BY_3);
        assertEquals(160, aspectRatio.getWidth());
        assertEquals(120, aspectRatio.getHeight());
    }

    @Test
    public void testAspectRatio_9By16() {
        TextToImageAspectRatio aspectRatio = new TextToImageAspectRatio(320, AspectRatios.RATIO_9_BY_16);
        assertEquals(180, aspectRatio.getWidth());
        assertEquals(320, aspectRatio.getHeight());
    }

    @Test
    public void testAspectRatio_9By21() {
        TextToImageAspectRatio aspectRatio = new TextToImageAspectRatio(420, AspectRatios.RATIO_9_BY_21);
        assertEquals(180, aspectRatio.getWidth());
        assertEquals(420, aspectRatio.getHeight());
    }

    @Test
    public void testGetAspectRatioAsString() {
        TextToImageAspectRatio aspectRatio = new TextToImageAspectRatio(100, AspectRatios.RATIO_4_BY_3);
        assertEquals("4:3", aspectRatio.getAspectRatioAsString());
    }
}