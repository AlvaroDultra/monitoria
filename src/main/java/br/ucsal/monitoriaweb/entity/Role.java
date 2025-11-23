package br.ucsal.monitoriaweb.entity;

public enum Role {
    ROLE_ADMIN("Administrador"),
    ROLE_PROFESSOR("Professor");

    private final String descricao;

    Role(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
