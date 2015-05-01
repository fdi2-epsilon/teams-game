package eu.unipv.epsilon.enigma.loader.levels.parser.defaults;

public interface IDefaultsFactory {

    FieldProvider getCollectionDefaults();

    FieldProvider getQuestDefaults(int index);

}
