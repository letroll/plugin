package fr.letroll.mesmangas.animestory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import fr.letroll.framework.Notification;
import fr.letroll.mesmangas.plugin.ISite;

public class AnimeStory extends Service {
	public final String tag = this.getClass().getSimpleName();
	private String adresseDuSite = "http://www.anime-story.com/mangas/";
	private Document doc;

	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Notification.log(tag, "start");
	}

	public void onDestroy() {
		super.onDestroy();
	}

	public IBinder onBind(Intent intent) {
		return addBinder;
	}

	private final ISite.Stub addBinder = new ISite.Stub() {

		@Override
		public String getNomDuSite() {
			return tag;
		}

		@Override
		public boolean isMe(String mUrl) throws RemoteException {
			return false;
		}

		@Override
		public int getPid() throws RemoteException {
			return 0;
		}

		@Override
		public String getCountry() {
			return "fr";
		}

		@Override
		public String getNomEncode2(String nomManga) throws RemoteException {
			return null;
		}

		@Override
		public String getNomEncode(String nomManga) throws RemoteException {
			return null;
		}

		@Override
		public List<String> getMangaList() throws RemoteException {
			List<String> array_manga = new ArrayList<String>();
			try {
				doc = Jsoup.connect(adresseDuSite).timeout(10000).get();
				Elements titles = doc.select("ul[class=clear] > li:not(.title) >div[class=left] > a");
				// Notification.log("nbr titre", ""+titles.size());
				int i = 0;
				for (org.jsoup.nodes.Element element : titles) {
					if (i > 0) {
						if (!element.text().contains("mise ??") && !element.text().contains("Mise ??")) {
							array_manga.add(element.text().toLowerCase());
						} else {
							String tmp = element.text();
							tmp = tmp.replace("(", "");
							tmp = tmp.replace("mise ?? jour", "");
							tmp = tmp.replace("Mise ?? jour", "");
							tmp = tmp.replace(")", "");
							array_manga.add(tmp.toLowerCase());
						}
					}
					i++;
				}
				i = 0;
				Collections.sort(array_manga);
			} catch (MalformedURLException e) {} catch (IOException e) {
				Notification.log(tag, "malform : " + adresseDuSite);
				e.printStackTrace();
			} catch (Exception e) {
				Notification.log(tag, "excep");
				e.printStackTrace();
			}

			return array_manga;
		}

		@Override
		public String getImageFromPageAndWrite(String chemin) throws RemoteException {
			return null;
		}

		@Override
		public String getImageAdr(String titre, String adresse, String chapitre) throws RemoteException {
			return null;
		}

		@Override
		public List<String> getChapitre(String nomManga) throws RemoteException {
			return null;
		}

		@Override
		public List<String> getListImages(String curUrl) throws RemoteException {
			return null;
		}
	};
}
