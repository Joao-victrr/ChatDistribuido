import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.Receiver;
import org.jgroups.View;
import java.util.Scanner;

public class ChatApp implements Receiver {

    private JChannel channel;
    private String username;

    public static void main(String[] args) throws Exception {
        new ChatApp().start();
    }

    private void start() throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite seu nome: ");
        username = scanner.nextLine();

        channel = new JChannel(); // configuração padrão
        channel.setReceiver(this);
        channel.connect("ChatGroup");

        System.out.println("\n🌐 Conectado ao grupo!");
        System.out.println("💬 Digite sua mensagem e pressione ENTER.\n");

        // Notifica entrada do usuário
        channel.send(null, "*** " + username + " entrou no chat ***");

        while(true) {
            String text = scanner.nextLine();
            channel.send(null, username + ": " + text);
        }
    }

    @Override
    public void receive(Message msg) {
        String content = msg.getObject().toString();

        // Formata mensagens de entrada/saída com destaque
        if (content.contains("***")) {
            System.out.println("\n📢 " + content + "\n");
        } else {
            System.out.println("💬 " + content);
        }
    }

    @Override
    public void viewAccepted(View view) {
        System.out.println("👥 Participantes conectados: " + view.size());
    }
}
