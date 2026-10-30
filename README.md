# Simulador de Substituição de Páginas

**Autores:** Pedro Pinheiro Barros Leal e Emmanuel Rosendo Parente Dias  
**Disciplina:** Projeto de Sistemas Operacionais  
**Repositório:** https://github.com/pedropleal/TrabalhoProjdeSist

Projeto em Java usando apenas Swing e ArrayList, com quatro métodos: FIFO, LRU, Relógio e Ótimo. Mostra o número de faltas de página e um gráfico simples.

## Como executar

É necessário ter o JDK instalado. Na pasta do projeto, execute:

```bash
javac -encoding UTF-8 -d out src/SimuladorPaginas.java
java -cp out SimuladorPaginas
```

No programa, digite a sequência de páginas, escolha a quantidade de quadros e clique em **Simular**.

**Exemplo:** `7 0 1 2 0 3 0 4 2 3 0 3 2` e 3 quadros de memória.

- FIFO: 10 faltas
- LRU: 9 faltas
- Relógio: 9 faltas
- Ótimo: 7 faltas

O programa inicia com a memória vazia. Um acesso à página ausente conta como uma falta de página.

## Arquivos
- `src/SimuladorPaginas.java`: interface e quatro algoritmos, em um único arquivo.
