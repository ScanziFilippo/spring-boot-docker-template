package it.paleocapa.mastroiannim;

import java.util.*;
import static java.util.Map.*;

import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class JavaBossBot extends TelegramLongPollingBot {

	@Autowired
    private Environment env;

	private static final Logger LOG = LoggerFactory.getLogger(JavaBossBot.class);
	 
	@Value("${telegram.username}") 
	private String botUsername;
    
	@Value("${telegram.token}") 
	private String botToken;

	public String getBotUsername() {
		//LOG.info(env.getProperty("botUsername"));
		LOG.info(botUsername);
		//LOG.info(System.getenv("telegram.username"));
		return botUsername;
	}

	@Override
	@Deprecated
	public String getBotToken() {
		//LOG.info(env.getProperty("botToken"));
		LOG.info(botToken);
		//LOG.info(System.getenv("telegram.token"));
		return botToken;
	}
	public List<Object> traduci(String stringa){
		String prodotto = "";
		int i;
		String prezzo = "";
		for(i = 0; i < stringa.length(); i++){
			if(stringa.charAt(i) != ' '){
				prodotto += stringa.charAt(i);
			}else{
				break;
			}
		}
		for(i = i+1; i < stringa.length(); i++){
			prezzo += stringa.charAt(i);
		}
		try
		{
			double num = Double.parseDouble(prezzo);
			return Arrays.asList(prodotto.toLowerCase(), num);
		}
		catch (NumberFormatException e)
		{
			return Arrays.asList(prodotto.toLowerCase(), null);
		}
	}
	public String traduciRimozione(String stringa){
		String prodotto = "";
		int i;
		for(i = 0; i < stringa.length(); i++){
			if(stringa.charAt(i) == ' ')break;
		}
		for(i = i+1; i < stringa.length(); i++){
			prodotto += stringa.charAt(i);
		}
		if(numeri.get(prodotto) != null){
			prodotto = numeri.get(prodotto);
		}
		return prodotto.toLowerCase();
	}
	Map<String, String> numeri = Map.ofEntries(
		entry("1", "bottiglia-acqua-frizzante"),
		entry("2", "bottiglia-acqua-naturale"),
		entry("3", "bottiglia-pepsi"),
		entry("4", "bottiglia-pepsi-limone"),
		entry("5", "bottiglia-the-san-benedetto-limone"),
		entry("6", "bottiglia-the-san-benedetto-pesca"),
		entry("7", "brioche-cioccolato"),
		entry("8", "brioche-marmellata"),
		entry("9", "brioche-vuota"),
		entry("10", "calzone"),
		entry("11", "hamburger"),
		entry("12", "lattina-pepsi-limone"),
		entry("13", "lattina-the-san-benedetto-limone"),
		entry("14", "lattina-the-san-benedetto-pesca"),
		entry("15", "menu-cotoletta"),
		entry("16", "menu-insalata"),
		entry("17", "menu-lasagna"),
		entry("18", "menu-pasta"),
		entry("19", "menu-pizza-bibita"),
		entry("20", "menu-riso"),
		entry("21", "panino-cordon-bleau"),
		entry("22", "panino-cotoletta"),
		entry("23", "panino-gourmet"),
		entry("24", "panino-wurstel"),
		entry("25", "panzerotto"),
		entry("26", "piadina-cotoletta-patatine-ketchup"),
		entry("27", "piadina-cotoletta-patatine-mayo"),
		entry("28", "piadina-cootto-fontina"),
		entry("29", "piadina-wurstel-patatine-ketchup"),
		entry("30", "piadina-wurstel-patatine-mayo"),
		entry("31", "pizza-piegata"),
		entry("32", "speck-brie"),
		entry("33", "toast-patate"),
		entry("34", "ventaglio")
	);

	Map<String, Double> prezzi = Map.ofEntries(
			entry("brioche-cioccolato", 0.90), 
			entry("brioche-marmellata", 0.90), 
			entry("brioche-vuota", 0.90), 
			entry("panino-wurstel", 1.50), 
			entry("panino-cotoletta", 2.00), 
			entry("hamburger", 2.00), 
			entry("panino-gourmet", 2.00), 
			entry("piadina-cotto-fontina", 2.00),
			entry("speck-brie", 1.50), 
			entry("piadina-wurstel-patatine-mayo", 2.50), 
			entry("piadina-wurstel-patatine-ketchup", 2.50), 
			entry("piadina-cotoletta-patatine-mayo", 2.80), 
			entry("piadina-cotoletta-patatine-ketchup", 2.80),
			entry("pizza-piegata", 1.50), 
			entry("panzerotto", 2.00), 
			entry("calzone", 2.00), 
			entry("toast-patate", 3.00), 
			entry("ventaglio", 2.00), 
			entry("panino-cordon-bleau", 2.00), 
			entry("lattina-the-san-benedetto-pesca", 0.60), 
			entry("lattina-the-san-benedetto-limone", 0.60), 
			entry("lattina-pepsi-limone", 0.60), 
			entry("bottiglia-acqua-naturale", 1.00), 
			entry("bottiglia-acqua-frizzante", 1.00), 
			entry("bottiglia-the-san-benedetto-pesca", 1.00), 
			entry("bottiglia-the-san-benedetto-limone", 1.00), 
			entry("bottiglia-pepsi-limone", 1.00), 
			entry("bottiglia-pepsi", 1.00), 
			entry("menu-pizza-bibita", 6.00), 
			entry("menu-pasta", 5.00), 
			entry("menu-lasagna", 5.00), 
			entry("menu-insalata", 5.00), 
			entry("menu-cotoletta", 5.00), 
			entry("menu-riso", 5.00)
		);
	public Double resto(String prodotto, Double pagato){
		return pagato - prezzi.get(prodotto.toLowerCase());
	}

	@Value("${segreto}") 
	String segreto;
	HashMap<String, Classe> classi = new HashMap<>();
	HashMap<String, String> segreti = new HashMap<>();
	HashMap<String, Utente> utenti = new HashMap<>();
	public void onUpdateReceived(Update update) {
		long chatId = update.getMessage().getChatId();
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
		String t;

		ReplyKeyboardMarkup tastiera = new ReplyKeyboardMarkup();
		tastiera.setSelective(true);
		tastiera.setResizeKeyboard(true);
		tastiera.setOneTimeKeyboard(false);

		KeyboardRow riga = new KeyboardRow();
		KeyboardRow riga2 = new KeyboardRow();
		KeyboardRow riga3 = new KeyboardRow();
		KeyboardButton pulsante = new KeyboardButton("/accedi");
		
        if (update.hasMessage() && update.getMessage().hasText()) {
			if(utenti.get(update.getMessage().getFrom().getUserName()) != null && utenti.get(update.getMessage().getFrom().getUserName()).stato != null){
				switch (utenti.get(update.getMessage().getFrom().getUserName()).stato) {
					case creazione:
						if(classi.get(update.getMessage().getText().toUpperCase()) == null){
							classi.put(update.getMessage().getText().toUpperCase(), new Classe(update.getMessage().getText().toUpperCase()));
							utenti.get(update.getMessage().getFrom().getUserName()).classe = update.getMessage().getText().toUpperCase();
							message.setText("Classe " + update.getMessage().getText().toUpperCase() + " creata.\nOra crea il codice per accederci");
							utenti.get(update.getMessage().getFrom().getUserName()).stato = Stato.creazione2;
						}else{
							message.setText("Ora inserisci il codice per " + update.getMessage().getText().toUpperCase());
							utenti.get(update.getMessage().getFrom().getUserName()).classe = update.getMessage().getText().toUpperCase();
							utenti.get(update.getMessage().getFrom().getUserName()).stato = Stato.segreto;
						}
						ReplyKeyboardRemove tastieraa = new ReplyKeyboardRemove(true);
						message.setReplyMarkup(tastieraa);
						break;
					case creazione2:
						segreti.put(utenti.get(update.getMessage().getFrom().getUserName()).classe, update.getMessage().getText().toUpperCase());
						utenti.get(update.getMessage().getFrom().getUserName()).acceduto = true;
						utenti.get(update.getMessage().getFrom().getUserName()).stato = null;
						riga.add(new KeyboardButton("/menu"));
						riga.add(new KeyboardButton("/lista"));
						riga.add(new KeyboardButton("/azzerra"));
						riga2.add(new KeyboardButton("/me"));
						riga2.add(new KeyboardButton("/ghiaccia"));
						riga3.add(new KeyboardButton("/esci"));	
						riga.remove(pulsante);
						tastiera.setKeyboard(List.of(riga, riga2, riga3));
						message.setReplyMarkup(tastiera);
						message.setText("Fatto");
						break;
					case segreto:
						if(update.getMessage().getText().toUpperCase().equals(segreti.get(utenti.get(update.getMessage().getFrom().getUserName()).classe))){
							message.setText("Acceduto con successo");
							utenti.get(update.getMessage().getFrom().getUserName()).acceduto = true;
							riga.add(new KeyboardButton("/menu"));
							riga.add(new KeyboardButton("/lista"));
							riga.add(new KeyboardButton("/azzerra"));
							riga2.add(new KeyboardButton("/me"));
							riga2.add(new KeyboardButton("/ghiaccia"));
							riga3.add(new KeyboardButton("/esci"));
							riga.remove(pulsante);
							tastiera.setKeyboard(List.of(riga, riga2, riga3));
							message.setReplyMarkup(tastiera);
							utenti.get(update.getMessage().getFrom().getUserName()).stato = null;
						}else{
							message.setText("Riprova");
						}
						break;
					default:
						break;
					}
			}else if(utenti.get(update.getMessage().getFrom().getUserName()) != null && utenti.get(update.getMessage().getFrom().getUserName()).acceduto){
				switch(update.getMessage().getText().toLowerCase()){
					case "/start":
						message.setText("Benvenuto! Come posso aiutarti?");
						message.setReplyMarkup(tastiera);
						break;
					case "ciao":
						message.setText("Ciao anche a te!");
						break;
					case "/esci":
						message.setText("Disconesso da " + utenti.get(update.getMessage().getFrom().getUserName()).classe);
						utenti.get(update.getMessage().getFrom().getUserName()).classe = null;
						utenti.get(update.getMessage().getFrom().getUserName()).acceduto = false;
						riga.add(new KeyboardButton("/accedi"));
						tastiera.setKeyboard(List.of(riga));
						message.setReplyMarkup(tastiera);
						break;
					case "/lista":
						if(classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).lista.size() <= 0){
							message.setText("Lista vuota");
						}else{
							t = classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).lista.stream().map(Prodotto::prodotto).reduce("Lista:\n", (subtotal, element) -> subtotal +  " - " + element + "\n");
							t += "\nTotale pagato: €" + classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).totale + "0\nTotale da pagare: €" + (classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).totale - classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).restoTotale) + "0\nResto totale: €" + (classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).restoTotale) + "0"; 
							message.setText(t);
							break;
						}
						break;
					case "/menu":
						t = numeri .keySet().stream().map(Integer::parseInt).sorted().map(p -> p.toString()).reduce("Menu:\n", (subtotal, element) -> subtotal + element.toString() + " - " + numeri.get(element) + "\n");
						//t = numeri.keySet().stream()..(p -> p).sorted().reduce("Menu:\n", (subtotal, element) -> subtotal + element.toString() + " - " + numeri.get(element) + "\n"); 
						//t = prezzi.keySet().stream().sorted().reduce("Menu:\n", (subtotal, element) -> subtotal + " - " + element + "\n");
						message.setText(t);
						break;
					case "/azzerra":
						if(utenti.get(update.getMessage().getFrom().getUserName()).amministratore){
							classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).lista = new LinkedList<Prodotto>();
							classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).totale = 0.0;
							classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).restoTotale = 0.0;
							message.setText("Lista azzerata");
						}else{
							message.setText("Devi essere amministratore per farlo");
						}
						break;
					case "/me":
						if(classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).lista.stream().filter(p -> p.nomeUtente.equals(update.getMessage().getFrom().getUserName())).count() <= 0){
							message.setText("Non hai comprato niente");
						}else{
							t = classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).lista.stream().filter(p -> p.nomeUtente.equals(update.getMessage().getFrom().getUserName())).map(Prodotto::prodotto).reduce("Lista:\n", (subtotal, element) -> subtotal +  " - " + element + "\n");
							t += "\nTotale pagato: €" + classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).lista.stream().filter(p -> p.nomeUtente.equals(update.getMessage().getFrom().getUserName())).mapToDouble(p -> p.prezzo).sum() + "0\nTotale da pagare: €" + (classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).lista.stream().filter(p -> p.nomeUtente.equals(update.getMessage().getFrom().getUserName())).mapToDouble(p -> p.prezzo).sum() - classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).lista.stream().filter(p -> p.nomeUtente.equals(update.getMessage().getFrom().getUserName())).mapToDouble(p -> p.resto).sum()) + "0\nResto totale: €" + (classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).lista.stream().filter(p -> p.nomeUtente.equals(update.getMessage().getFrom().getUserName())).mapToDouble(p -> p.resto).sum()) + "0";
							message.setText(t);
						}
						break;
					case "/ghiaccia":
						if(utenti.get(update.getMessage().getFrom().getUserName()).amministratore){
							//classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).ghiacciata = classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).ghiacciata ? {false; message.setText("")} : true;
							if(classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).ghiacciata){
								classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).ghiacciata = false;
								message.setText("Lista sghiacciata");
							}else{
								classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).ghiacciata = true;
								message.setText("Lista ghiacciata");
							}
						}else{
							message.setText("Devi essere amministratore per farlo");
						}
						break;
					default:
						if(update.getMessage().getText().toLowerCase().equals(segreto)){
							if(utenti.get(update.getMessage().getFrom().getUserName()).amministratore){
								utenti.get(update.getMessage().getFrom().getUserName()).amministratore = false;
								message.setText("Ora non sei più amministratore");
							}else{
								utenti.get(update.getMessage().getFrom().getUserName()).amministratore = true;
								message.setText("Ora sei amministratore");
							}
						}
						else{
							List<Object> prova = traduci(update.getMessage().getText());
							String prodotto = prova.get(0).toString();
							if(prodotto.equals("rimuovi")){
								final String prodottoo = traduciRimozione(update.getMessage().getText());
								if(prezzi.get(prodottoo) == null){
									message.setText("Prodotto non valido.");
								}else if(!classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).ghiacciata){
									Prodotto pro = classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).lista.stream().filter(p -> p.nomeUtente.equals(update.getMessage().getFrom().getUserName()) && p.nomeProdotto.equals(prodottoo)).findFirst().orElse(null);
									if(pro == null){
										message.setText("Non hai comprato " + prodottoo);
									}else{
										message.setText("Bene, tolgo " + prodottoo + "\nStai riprendendo €" + pro.prezzo + "0\n");
										classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).lista.remove(pro);
										classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).totale -= pro.prezzo;
										classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).restoTotale -= pro.resto;
									}
								}else{
									message.setText("La lista è ghiacciata, non puoi rimuovere prodotti.");
								}
							}else{
								if(numeri.get(prodotto) != null){
									prodotto = numeri.get(prodotto);
								}
								if(prezzi.get(prodotto) == null){
									message.setText("Prodotto non valido.");
								}else if(prova.get(1) == null){
									message.setText("Prezzo non valido.");
								}else if(!classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).ghiacciata){
									Double pagato = Double.parseDouble(prova.get(1).toString());
									if(resto(prodotto, pagato) > 0){
										message.setText("Bene, aggiungo " + prodotto + "\nStai pagando €" + prova.get(1) + "0\nAvrai di resto €" + resto(prodotto, pagato)+"0");
										classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).lista.add(new Prodotto(prodotto, update.getMessage().getFrom().getUserName(), update.getMessage().getFrom().getFirstName(), pagato, resto(prodotto, pagato)));
										classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).totale += pagato;
										classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).restoTotale += resto(prodotto, pagato);
									}else if(resto(prodotto, pagato) == 0){
										message.setText("Bene, aggiungo " + prodotto + "\nStai pagando €" + prova.get(1) + "0\nNiente resto");
										classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).lista.add(new Prodotto(prodotto, update.getMessage().getFrom().getUserName(), update.getMessage().getFrom().getFirstName(), pagato, resto(prodotto, pagato)));
										classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).totale += pagato;
										classi.get(utenti.get(update.getMessage().getFrom().getUserName()).classe).restoTotale += resto(prodotto, pagato);
									}else{
										message.setText("Male, il prezzo è €" + prezzi.get(prodotto) + "0 ma vuoi pagare solo con €" + prova.get(1) + "0 😠");
									}
								}else{
									message.setText("La lista è ghiacciata, non puoi aggiungere prodotti.");
								}
							}
						}
						riga.add(new KeyboardButton("/menu"));
						riga.add(new KeyboardButton("/lista"));
						riga.add(new KeyboardButton("/azzerra"));
						riga2.add(new KeyboardButton("/me"));
						riga2.add(new KeyboardButton("/ghiaccia"));
						riga3.add(new KeyboardButton("/esci"));						tastiera.setKeyboard(List.of(riga, riga2, riga3));				
						message.setReplyMarkup(tastiera);
						break;
				}
			}    
			else{
				switch(update.getMessage().getText().toLowerCase()){
					case "/start":
						message.setText("Benvenuto! Come posso aiutarti?");
						if(utenti.get(update.getMessage().getFrom().getUserName()) == null){
							utenti.put(update.getMessage().getFrom().getUserName(), new Utente());
						}

						riga.add(pulsante);
						tastiera.setKeyboard(List.of(riga));				
						message.setReplyMarkup(tastiera);
						break;
					case "ciao":
						message.setText("Ciao anche a te!");
						break;
					case "/accedi":
						message.setText("Inserisci il nome della classe che vuoi creare o scegli quella a cui vuoi accedere");
						if(classi.size() == 0){
							ReplyKeyboardRemove tastieraa = new ReplyKeyboardRemove(true);
							message.setReplyMarkup(tastieraa);
						}else{
							classi.keySet().stream().sorted().forEach(c -> riga.add(new KeyboardButton(c)));
							tastiera.setKeyboard(List.of(riga));				
							message.setReplyMarkup(tastiera);	
						}
						utenti.get(update.getMessage().getFrom().getUserName()).stato = Stato.creazione;
						break;
				}        
			}
        }else{
            message.setText("Scusa, non capisco");
        }

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
	}
}
