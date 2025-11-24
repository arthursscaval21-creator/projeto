import com.sun.jna.Library;
import com.sun.jna.Native;
import java.util.Scanner;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.FileInputStream;

public class Main {

    // Interface que representa a DLL, usando JNA
    public interface ImpressoraDLL extends Library {

        // Caminho completo para a DLL
        ImpressoraDLL INSTANCE = (ImpressoraDLL) Native.load(
                "C:\\Users\\arthur_sousa02\\Downloads\\Java-Aluno Graduacao\\Java-Aluno Graduacao\\E1_Impressora01.dll",
                ImpressoraDLL.class
        );


        private static String lerArquivoComoString(String path) throws IOException {
            FileInputStream fis = new FileInputStream(path);
            byte[] data = fis.readAllBytes();
            fis.close();
            return new String(data, StandardCharsets.UTF_8);
        }


        int AbreConexaoImpressora(int tipo, String modelo, String conexao, int param);
        int FechaConexaoImpressora();
        int ImpressaoTexto(String dados, int posicao, int estilo, int tamanho);
        int Corte(int avanco);
        int ImpressaoQRCode(String dados, int tamanho, int nivelCorrecao);
        int ImpressaoCodigoBarras(int tipo, String dados, int altura, int largura, int HRI);
        int AvancaPapel(int linhas);
        int StatusImpressora(int param);
        int AbreGavetaElgin();
        int AbreGaveta(int pino, int ti, int tf);
        int SinalSonoro(int qtd, int tempoInicio, int tempoFim);
        int ModoPagina();
        int LimpaBufferModoPagina();
        int ImprimeModoPagina();
        int ModoPadrao();
        int PosicaoImpressaoHorizontal(int posicao);
        int PosicaoImpressaoVertical(int posicao);
        int ImprimeXMLSAT	(String dados, int param);
        int ImprimeXMLCancelamentoSAT(String dados, String assQRCode, int param);
    }

    private static boolean conexaoAberta = false;
    private static int tipo;
    private static String modelo;
    private static String conexao;
    private static int parametro;

    private static final Scanner scanner = new Scanner(System.in);

    private static String capturarEntrada(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    public static void configurarConexao() {
        if (!conexaoAberta) {
            System.out.println("Digite o tipo da conexão (ex: 1):");
            tipo = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Digite o modelo (ex: i9):");
            modelo = scanner.nextLine();

            System.out.println("Digite a conexão (ex: USB):");
            conexao = scanner.nextLine();

            System.out.println("Digite o parâmetro (ex: 0):");
            parametro = scanner.nextInt();
            scanner.nextLine();
        }
    }

    public static void abrirConexao() {
        int retorno = ImpressoraDLL.INSTANCE.AbreConexaoImpressora(tipo, modelo, conexao, parametro);
        if (retorno == 0) {
            conexaoAberta = true;
            System.out.println("Conexão abrida com sucesso!");
        } else {
            System.out.println("Erro ao abrir conexão! Retorno: " + retorno);
        }
    }

    public static void fecharConexao() {
        if (conexaoAberta) {
            ImpressoraDLL.INSTANCE.FechaConexaoImpressora();
            conexaoAberta = false;
            System.out.println("Conexão fechada!");
        }
    }

    public static void impressaoTexto() {

        if (conexaoAberta) {
            System.out.println("Digite o texto que deseja imprimir:");
            String dados = scanner.nextLine();
            int retorno = ImpressoraDLL.INSTANCE.ImpressaoTexto(dados, 1, 4, 0);
            System.out.println("Retorno: " + retorno);
            ImpressoraDLL.INSTANCE.Corte(6);
        } else {
            System.out.println("Erro: impressora não conectada!");
        }
    }

    public static void impressaoQRCode() {

        if (conexaoAberta) {
            System.out.println("Digite o texto que deseja imprimir:");
            String dados = scanner.nextLine();
            int retorno = ImpressoraDLL.INSTANCE.ImpressaoQRCode(dados, 6, 4);
            if (retorno == 0) {
                System.out.println("Retorno: " + retorno);
                ImpressoraDLL.INSTANCE.Corte(6);
            } else {
                System.out.println("Erro: impressora não conectada!");
            }
        }
    }
    public static void impressaoCodigoBarras() {

        if (conexaoAberta) {
            int retorno = ImpressoraDLL.INSTANCE.ImpressaoCodigoBarras(8, "{A012345678912", 100, 2, 3);
            if (retorno == 0) {
                System.out.println("Retorno: " + retorno);
                ImpressoraDLL.INSTANCE.Corte(6);
            }
        } else {
            System.out.println("Erro: impressora não conectada!");
        }
    }

    public static void impressaoXMLSAT() {
        String dados = "path=C:\\Users\\arthur_sousa02\\Downloads\\Java-Aluno Graduacao\\Java-Aluno Graduacao\\XMLSAT.xml";


        if (conexaoAberta) {
            int retorno = ImpressoraDLL.INSTANCE.ImprimeXMLSAT(dados, 0);
            if (retorno == 0) {
                System.out.println("Retorno impressão XML SAT: " + retorno);
                ImpressoraDLL.INSTANCE.Corte(6);
            }
        } else {
            System.out.println("Erro: impressora não conectada!");
        }
    }

    public static void impressaoXMLCancelamentoSAT() {

        String dados = "path=C:\\Users\\arthur_sousa02\\Downloads\\Java-Aluno Graduacao\\Java-Aluno Graduacao\\CANC_SAT.xml" ;

        String assQRCode = "Q5DLkpdRijIRGY6YSSNsTWK1TztHL1vD0V1Jc4spo/CEUqICEb9SFy82ym8EhBRZjbh3btsZhF+sjHqEMR159i4agru9x6KsepK/q0E2e5xlU5cv3m1woYfgHyOkWDNcSdMsS6bBh2Bpq6s89yJ9Q6qh/J8YHi306ce9Tqb/drKvN2XdE5noRSS32TAWuaQEVd7u+TrvXlOQsE3fHR1D5f1saUwQLPSdIv01NF6Ny7jZwjCwv1uNDgGZONJdlTJ6p0ccqnZvuE70aHOI09elpjEO6Cd+orI7XHHrFCwhFhAcbalc+ZfO5b/+vkyAHS6CYVFCDtYR9Hi5qgdk31v23w==";

        if (conexaoAberta) {
            int retorno = ImpressoraDLL.INSTANCE.ImprimeXMLCancelamentoSAT(dados, assQRCode, 0);
            if (retorno == 0) {
                System.out.println("Retorno impressão XML Canc SAT: " + retorno);
                ImpressoraDLL.INSTANCE.Corte(6);
            }
        } else {
            System.out.println("Erro: impressora não conectada!");
        }
    }

    public static void abrirGavetaElgin() {
        if (conexaoAberta) {
            ImpressoraDLL.INSTANCE.AbreGavetaElgin();
            System.out.println("Gaveta aberta!");
        } else {
            System.out.println("Erro: impressora não conectada!");
        }
    }

    public static void abrirGaveta() {
        System.out.println("Digite o pino da gaveta:");
        int pino = scanner.nextInt();
        scanner.nextLine();
        if (conexaoAberta) {
            ImpressoraDLL.INSTANCE.AbreGaveta(1 , 5 , 10);
            System.out.println("Gaveta aberta!");
        } else {
            System.out.println("Erro: impressora não conectada!");
        }
    }

    public static void sinalSonoro() {

        if (conexaoAberta) {
            ImpressoraDLL.INSTANCE.SinalSonoro(4, 5, 5 );
            System.out.println("Sinal sonoro executado!");
        } else {
            System.out.println("Erro: impressora não conectada!");
        }
    }

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n*************************************************");
            System.out.println("**************** MENU IMPRESSORA *******************");
            System.out.println("*************************************************\n");

            System.out.println("1  - Configurar Conexao");
            System.out.println("2  - Abrir Conexao");
            System.out.println("3  - Impressao Texto");
            System.out.println("4  - Impressao QRCode");
            System.out.println("5  - Impressao Cod Barras");
            System.out.println("6  - Impressao XML SAT");
            System.out.println("7  - Impressao XML Canc SAT");
            System.out.println("8  - Abrir Gaveta Elgin");
            System.out.println("9  - Abrir Gaveta");
            System.out.println("10 - Sinal Sonoro");
            System.out.println("0  - Fechar Conexao e Sair");
            System.out.println("--------------------------------------");

            String escolha = capturarEntrada("\nDigite a opção desejada: ");

            switch (escolha) {
                case "1":
                    configurarConexao();
                    break;
                case "2":
                    abrirConexao();
                    break;
                case "3":
                    impressaoTexto();
                    break;
                case "4":
                    impressaoQRCode();
                    break;
                case "5":
                    impressaoCodigoBarras();
                    break;
                case "6":
                    impressaoXMLSAT();
                    break;
                case "7":
                    impressaoXMLCancelamentoSAT();
                    break;
                case "8":
                    abrirGavetaElgin();
                    break;
                case "9":
                    abrirGaveta();
                    break;
                case "10":
                    sinalSonoro();
                    break;
                case "0":
                    fecharConexao();
                    System.out.println("Saindo...");
                    System.exit(0);
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}