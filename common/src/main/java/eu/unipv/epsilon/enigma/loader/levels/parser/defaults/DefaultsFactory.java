package eu.unipv.epsilon.enigma.loader.levels.parser.defaults;

import eu.unipv.epsilon.enigma.loader.levels.CollectionContainer;

public class DefaultsFactory implements IDefaultsFactory {

    private CollectionContainer context;

    public DefaultsFactory(CollectionContainer context) {
        this.context = context;
    }

    @Override
    public FieldProvider getCollectionDefaults() {
        return new CollectionDefaults(context);
    }

    @Override
    public FieldProvider getQuestDefaults(int index) {
        return new QuestDefaults(context, index);
    }

}
