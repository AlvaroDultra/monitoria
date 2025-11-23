package br.ucsal.monitoriaweb.entity;

public enum TipoMonitoria {
    PRESENCIAL("Presencial"),
    REMOTO("Remoto");

    private final String descricao;

    TipoMonitoria(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
