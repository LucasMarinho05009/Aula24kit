package br.com.starlog.model;

/**
 * CAMADA MODEL (Modelo de Domínio)
 * Representa uma carga a ser despachada pela estação StarLog.
 *
 * ATENÇÃO: Esta classe NÃO importa nenhum componente visual do Swing!
 */
public class Carga {
    private final String codigoRastreio;
    private final String categoria;
    private final double peso;
    private final double valorDeclarado;

    public Carga(String codigoRastreio, String categoria, double peso, double valorDeclarado) {
        // BARREIRA FAIL-FAST: valida tudo antes de qualquer atribuição
        if (codigoRastreio == null || codigoRastreio.trim().isEmpty()) {
            throw new IllegalArgumentException("Código de rastreio não pode ser nulo ou vazio.");
        }
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria não pode ser nula ou vazia.");
        }
        // "!(x > 0)" também rejeita NaN; isInfinite rejeita "Infinity" digitado na tela
        if (!(peso > 0) || Double.isInfinite(peso)) {
            throw new IllegalArgumentException("Peso deve ser estritamente positivo (maior que zero).");
        }
        if (!(valorDeclarado > 0) || Double.isInfinite(valorDeclarado)) {
            throw new IllegalArgumentException("Valor declarado deve ser maior que zero.");
        }

        this.codigoRastreio = codigoRastreio.trim().toUpperCase();
        this.categoria = categoria.trim().toUpperCase();
        this.peso = peso;
        this.valorDeclarado = valorDeclarado;
    }

    public String getCodigoRastreio() {
        return codigoRastreio;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getPeso() {
        return peso;
    }

    public double getValorDeclarado() {
        return valorDeclarado;
    }

    /**
     * Regra de Negócio: Cálculo do Seguro Espacial da Carga.
     * Taxa base de 2% do valor declarado; cargas perigosas ou com mais de 100 kg
     * recebem sobretaxa fixa de R$ 150,00 de risco operacional.
     */
    public double calcularSeguro() {
        double seguro = this.valorDeclarado * 0.02;
        if ("PERIGOSA".equals(this.categoria) || this.peso > 100.0) {
            seguro += 150.0;
        }
        return seguro;
    }

    @Override
    public String toString() {
        return String.format("[%s] Categoria: %s | Peso: %.2f kg | Valor: R$ %.2f | Seguro: R$ %.2f",
                codigoRastreio, categoria, peso, valorDeclarado, calcularSeguro());
    }
}
