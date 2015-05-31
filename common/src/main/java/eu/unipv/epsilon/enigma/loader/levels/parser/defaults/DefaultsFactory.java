package eu.unipv.epsilon.enigma.loader.levels.parser.defaults;

public class DefaultsFactory implements IDefaultsFactory {

    private ContentChecker context;

    public DefaultsFactory(ContentChecker context) {
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
