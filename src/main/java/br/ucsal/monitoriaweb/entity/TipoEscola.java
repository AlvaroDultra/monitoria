package br.ucsal.monitoriaweb.entity;

public enum TipoEscola {
    EDUCACAO_CULTURA_HUMANIDADES("Escola de Educação, Cultura e Humanidades"),
    CIENCIAS_SOCIAIS_APLICADAS("Escola de Ciências Sociais e Aplicadas"),
    ENGENHARIAS_CIENCIAS_TECNOLOGICAS("Engenharias e Ciências Tecnológicas"),
    CIENCIAS_NATURAIS_SAUDE("Escola de Ciências Naturais e da Saúde");

    private final String descricao;

    TipoEscola(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
