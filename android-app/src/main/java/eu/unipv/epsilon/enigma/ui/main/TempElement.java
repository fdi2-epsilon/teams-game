package eu.unipv.epsilon.enigma.ui.main;

import android.content.res.Resources;
import android.support.annotation.StringRes;

public class TempElement {

    private String title, subtitle, description;
    private CardType type;

    public TempElement(CardType type) {
        this.type = type;

        if (type != CardType.FIRST_START) {
            this.title = "";
            this.subtitle = "";
            this.description = "";
        }
    }

    public TempElement(Resources resources, @StringRes int titRes, @StringRes int subRes, @StringRes int dscRes, CardType type) {
        this.type = type;

        if (type != CardType.FIRST_START) {
            this.title = titRes != 0 ? resources.getString(titRes) : "";
            this.subtitle = subRes != 0 ? resources.getString(subRes) : "";
            this.description = dscRes != 0 ? resources.getString(dscRes) : "";
        }
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }

    public CardType getType() {
        return type;
    }

}
