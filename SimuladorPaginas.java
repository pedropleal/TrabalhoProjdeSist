import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SimuladorPaginas {
    // FIFO: remove a pagina que entrou primeiro.
    static int fifo(int[] paginas, int quadros) {
        ArrayList<Integer> memoria = new ArrayList<>();
        int faltas = 0;
        for (int pagina : paginas) {
            if (!memoria.contains(pagina)) {
                faltas++;
                if (memoria.size() == quadros) memoria.remove(0);
                memoria.add(pagina);
            }
        }
        return faltas;
    }

    // LRU: coloca a pagina usada no fim da lista.
    static int lru(int[] paginas, int quadros) {
        ArrayList<Integer> memoria = new ArrayList<>();
        int faltas = 0;
        for (int pagina : paginas) {
            if (memoria.contains(pagina)) {
                memoria.remove(Integer.valueOf(pagina));
            } else {
                faltas++;
                if (memoria.size() == quadros) memoria.remove(0);
            }
            memoria.add(pagina);
        }
        return faltas;
    }

    // Relogio: pagina com bit 1 recebe uma segunda chance.
    static int relogio(int[] paginas, int quadros) {
        int[] memoria = new int[quadros];
        int[] bits = new int[quadros];
        boolean[] ocupados = new boolean[quadros];
        int ponteiro = 0, faltas = 0;
        for (int pagina : paginas) {
            int posicao = -1;
            for (int i = 0; i < quadros; i++) {
                if (ocupados[i] && memoria[i] == pagina) posicao = i;
            }
            if (posicao != -1) {
                bits[posicao] = 1;
            } else {
                faltas++;
                while (ocupados[ponteiro] && bits[ponteiro] == 1) {
                    bits[ponteiro] = 0;
                    ponteiro = (ponteiro + 1) % quadros;
                }
                memoria[ponteiro] = pagina;
                ocupados[ponteiro] = true;
                bits[ponteiro] = 1;
                ponteiro = (ponteiro + 1) % quadros;
            }
        }
        return faltas;
    }

    // Otimo: remove a pagina que sera usada mais tarde.
    static int otimo(int[] paginas, int quadros) {
        ArrayList<Integer> memoria = new ArrayList<>();
        int faltas = 0;
        for (int i = 0; i < paginas.length; i++) {
            if (memoria.contains(paginas[i])) continue;
            faltas++;
            if (memoria.size() == quadros) {
                int vitima = 0, maisDistante = -1;
                for (int j = 0; j < memoria.size(); j++) {
                    int proximoUso = paginas.length;
                    for (int k = i + 1; k < paginas.length; k++) {
                        if (paginas[k] == memoria.get(j)) {
                            proximoUso = k;
                            break;
                        }
                    }
                    if (proximoUso > maisDistante) {
                        maisDistante = proximoUso;
                        vitima = j;
                    }
                }
                memoria.remove(vitima);
            }
            memoria.add(paginas[i]);
        }
        return faltas;
    }

    public static void main(String[] args) {
        JFrame janela = new JFrame("Simulador de Paginas");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setSize(650, 500);
        janela.setLayout(new BorderLayout(10, 10));

        JPanel painel = new JPanel(new GridLayout(5, 1, 5, 5));
        JTextField entrada = new JTextField("7 0 1 2 0 3 0 4 2 3 0 3 2");
        JTextField quantidade = new JTextField("3");
        JButton botao = new JButton("Simular");
        JTextArea resultado = new JTextArea(5, 20);
        resultado.setEditable(false);
        painel.add(new JLabel("Sequencia de paginas (separadas por espaco):"));
        painel.add(entrada);
        painel.add(new JLabel("Quantidade de quadros:"));
        painel.add(quantidade);
        painel.add(botao);

        // Grafico simples, desenhado pelo proprio Swing.
        JPanel grafico = new JPanel() {
            
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                String[] nomes = {"FIFO", "LRU", "Relogio", "Otimo"};
                int[] valores = (int[]) getClientProperty("faltas");
                if (valores == null) valores = new int[]{0, 0, 0, 0};
                int maior = 1;
                for (int v : valores) maior = Math.max(maior, v);
                for (int i = 0; i < 4; i++) {
                    int y = 20 + i * 47;
                    int largura = 360 * valores[i] / maior;
                    g.setColor(Color.BLACK);
                    g.drawString(nomes[i], 15, y + 17);
                    g.setColor(new Color(60, 120, 210));
                    g.fillRect(90, y, largura, 25);
                    g.setColor(Color.BLACK);
                    g.drawString("" + valores[i], 96 + largura, y + 17);
                }
            }
        };
        grafico.setPreferredSize(new Dimension(600, 220));

        botao.addActionListener(e -> {
            try {
                String[] partes = entrada.getText().trim().split("[,;\\s]+");
                int[] paginas = new int[partes.length];
                for (int i = 0; i < partes.length; i++) paginas[i] = Integer.parseInt(partes[i]);
                int quadros = Integer.parseInt(quantidade.getText().trim());
                if (quadros < 1) throw new NumberFormatException();
                int[] faltas = {fifo(paginas, quadros), lru(paginas, quadros),
                                relogio(paginas, quadros), otimo(paginas, quadros)};
                resultado.setText("Metodo 1 - FIFO: " + faltas[0] + " faltas de pagina\n"
                        + "Metodo 2 - LRU: " + faltas[1] + " faltas de pagina\n"
                        + "Metodo 3 - Relogio: " + faltas[2] + " faltas de pagina\n"
                        + "Metodo 4 - Otimo: " + faltas[3] + " faltas de pagina");
                // Atualiza o painel do grafico sem bibliotecas externas.
                grafico.putClientProperty("faltas", faltas);
                grafico.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(janela, "Digite apenas numeros inteiros e quadros maiores que zero.");
            }
        });
        janela.add(painel, BorderLayout.NORTH);
        janela.add(new JScrollPane(resultado), BorderLayout.CENTER);
        janela.add(grafico, BorderLayout.SOUTH);
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);
        botao.doClick();
    }
}
