package io.github.lczx.enigmatemplates;

import java.util.Map;

public class PuzzleItem {

    private String imgPath;
    private String initPos;
    private String targetPos;
    private Dimensions position;

    public PuzzleItem(Map<String, String> attrs, Dimensions defaultPos) {
        imgPath = getOrDefault(attrs, "img", ""); // ok if ""
        initPos = getOrDefault(attrs, "init-pos", "0");
        targetPos = getOrDefault(attrs, "pos", "0");

        if (attrs.containsKey("pivot"))
            position = Dimensions.fromXmlString(attrs.get("pivot"));
        else
            position = defaultPos;
    }

    public String toJson() {
        return String.format("{img: '%s', pos: %s, c: %s, t: %s}, ",
                imgPath, position, initPos, targetPos);
    }

    // Reimplementing Java 8 (90% copy-paste)
    private static <S, T> T getOrDefault(Map<S, T> map, S key, T defaultValue) {
        T v;
        return (((v = map.get(key)) != null) || map.containsKey(key))
                ? v
                : defaultValue;
    }

}
