package br.ucsal.monitoriaweb.entity;

public enum FormacaoProfessor {
    GRADUACAO("Graduação"),
    ESPECIALIZACAO("Especialização"),
    MBA("MBA"),
    MESTRADO("Mestrado"),
    DOUTORADO("Doutorado"),
    POS_DOUTORADO("Pós-Doutorado");

    private final String descricao;

    FormacaoProfessor(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
