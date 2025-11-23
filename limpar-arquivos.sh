#!/bin/bash

# Script para limpar arquivos de teste e documentação temporária
# MonitoriaWeb - Limpeza de arquivos não essenciais

echo "🧹 Iniciando limpeza de arquivos..."
echo ""

cd /home/alvaro/Downloads/monitoriaweb--/

# Contar arquivos antes
TOTAL_ANTES=$(ls -1 | wc -l)

echo "📊 Arquivos na raiz antes da limpeza: $TOTAL_ANTES"
echo ""

# Apagar scripts de teste Python (mantendo test-all-endpoints.py)
echo "🗑️  Apagando scripts de teste Python antigos..."
rm -f test-endpoints.py
rm -f test-frequencias.py
rm -f test-frequencias-correto.py

# Apagar scripts Shell de teste
echo "🗑️  Apagando scripts Shell de teste..."
rm -f test-completo.sh
rm -f test-curl.sh
rm -f test-endpoints.sh
rm -f test-simple.sh

# Apagar logs temporários
echo "🗑️  Apagando logs temporários..."
rm -f test-results-*.log

# Apagar documentação duplicada (mantendo TESTES-COMPLETOS-FINAL.md e RELATORIO-FINAL.txt)
echo "🗑️  Apagando documentação duplicada..."
rm -f TESTES.md
rm -f TESTES-RESULTADOS.md
rm -f RESULTADOS_TESTES.md
rm -f RESULTADOS_FINAIS_TESTES.md
rm -f RESUMO-TESTES.txt
rm -f CORRECOES.md

# Apagar scripts de setup admin
echo "🗑️  Apagando scripts de setup admin..."
rm -f criar-admin.sh
rm -f criar-admin-rapido.sh
rm -f criar-admin.sql
rm -f criar-usuario-admin.java
rm -f atualizar-senha-admin.sh
rm -f COMO_CRIAR_ADMIN.md

# OPCIONAL: Descomentar a linha abaixo para apagar endpoints-tests.http
# rm -f endpoints-tests.http

echo ""
echo "✅ Limpeza concluída!"
echo ""

# Contar arquivos depois
TOTAL_DEPOIS=$(ls -1 | wc -l)
REMOVIDOS=$((TOTAL_ANTES - TOTAL_DEPOIS))

echo "📊 Arquivos na raiz depois da limpeza: $TOTAL_DEPOIS"
echo "🗑️  Total de arquivos removidos: $REMOVIDOS"
echo ""

echo "📁 Arquivos mantidos:"
ls -1 *.{xml,md,txt,py,http} 2>/dev/null | grep -E "(pom.xml|.gitignore|README.md|test-all-endpoints.py|TESTES-COMPLETOS-FINAL.md|RELATORIO-FINAL.txt|endpoints-tests.http|ARQUIVOS-PARA-LIMPAR.md)" || ls -1

