package CommanLine;

import commandLineMenus.*;
import commandLineMenus.rendering.examples.util.InOut;
import java.util.ArrayList;

import org.hibernate.Session;

import inscriptions.*;

import static commandLineMenus.rendering.examples.util.InOut.*;

import java.time.LocalDate;

public class mainUser {

	public void start() {
		menuPrincipal().start();

	}

	private Menu menuPrincipal() {
		Menu menu = new Menu("Bienvenue sur Inscriptions Sportives");
		menu.add(menuCompetitions());
		menu.add(menuPersonne());
		menu.add(menuEquipe());
		menu.addQuit("q");
		return menu;
	}

	@SuppressWarnings("unused")
	private Option Quit() {
		Menu menu = new Menu("Quitter", "q");
		menu.addBack("r");
		return menu;
	}

	private Menu menuCompetitions() {
		Menu menu = new Menu("Gestion des comp�titions", "c");

		menu.add(listCompetition());

		menu.add(addCompetition());

		menu.add(selectCompetition());

		menu.addBack("b");
		menu.setAutoBack(true);
		return menu;
	}

	private Option listCompetition() {
		return new Option("Liste des comp�titions", "l", () -> {
			System.out.println("Liste des Competitions :");
			System.out.println(Inscriptions.getInscriptions().getCompetitions());
		});
		
		
		
	}

	private List<Competition> selectCompetition() {
		return new List<Competition>("S�lectionner une comp�tition", "s",
				() -> new ArrayList<>(Inscriptions.getInscriptions().getCompetitions()),
				(element) -> editCompetition(element));
	}

	private Menu editCompetition(Competition competition) {
		Menu menu = new Menu(
				"Editer " + competition.getNom()
						+ ((competition.inscriptionsOuvertes()) ? "" : " Inscriptions closes."),
				competition.getNom(), "");
		menu.add(listCompetition(competition));
		menu.add(listCandidat(competition));

		if (competition.inscriptionsOuvertes()) {
			if (!competition.estEnEquipe())
				menu.add(addPersonneCompetition(competition));
			else
				menu.add(addEquipeCompetition(competition));
		}

		menu.add(removeCandidat(competition));
		menu.add(editerCompetition(competition));
		menu.add(removeCompetition(competition));
		menu.addBack("b");
		menu.setAutoBack(true);
		return menu;
	}

	@SuppressWarnings("unused")
	private Option addCompetition() {
		return new Option("Ajouter une comp�tition", "a", () -> {

			try {
				Inscriptions.getInscriptions().createCompetition(InOut.getString("nom : "),
						LocalDate.of(InOut.getInt("Annee:"), InOut.getInt("Mois:"), InOut.getInt("Jour:")),
						InOut.getInt("0 - Comp�tition Seul \n1 - Comp�tition en Equipe : ") == 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private Option listCompetition(Competition competition) {
		return new Option("Detail de la competition", "l", () -> {
			System.out.println(competition.getNom());
			System.out.println(competition.getDateCloture());
			System.out.println(competition.estEnEquipe());

		});
	}

	private Option listCandidat(Competition competition) {
		return new Option("Afficher les candidats", "z", () -> {
			System.out.println(competition.getCandidats());
		});
	}

	private List<Candidat> addPersonneCompetition(Competition competition) {

		return new List<>("Ajouter une personne dans la comp�tition", "p",
				() -> new ArrayList<>(Inscriptions.getInscriptions().getCandidats()), (index, element) -> {
					competition.add((Personne) element);
				});
	}

	private List<Candidat> addEquipeCompetition(Competition competition) {
		return new List<>("Ajouter une �quipe dans la comp�tition", "e",
				() -> new ArrayList<>(Inscriptions.getInscriptions().getEquipes()), (index, element) -> {
					competition.add((Equipe) element);
				});
	}

	private List<Candidat> removeCandidat(Competition competition) {
		return new List<>("Supprimer un candidat", "s", () -> new ArrayList<>(competition.getCandidats()),
				(index, element) -> {
					competition.remove(element);
				});
	}

	private Option editerCompetition(Competition competition) {
		return new Option("Modifier une comp�tition", "m", () -> {
			competition.setNom(getString("Nouveau nom : \n"));
			competition.setDateCloture(competition.getDateCloture());
		});
	}

	private Option removeCompetition(Competition competition) {
		return new Option("Supprimer une comp�tition", "ss", () -> {
			competition.delete();
		});
	}

	private Menu menuPersonne() {
		Menu menu = new Menu("Gestion des personnes", "p");

		menu.add(listPersonne());

		menu.add(addPersonne());

		menu.add(selectPersonne());

		menu.addBack("b");
		menu.setAutoBack(true);
		return menu;
	}

	private Option listPersonne() {
		return new Option("Liste des personnes", "l", () -> {
			System.out.println(Inscriptions.getInscriptions().getPersonnes());
		});

	}

	private Option addPersonne() {
		return new Option("Ajouter une nouvelle personne", "a", () -> {
			Inscriptions.getInscriptions().createPersonne(getString("Nom : \n"), getString("Pr�nom : \n"),
					getString("Mail : \n"));
		});
	}

	private Option selectPersonne() {
		return new List<Personne>("S�lectionner une personne", "s",
				() -> new ArrayList<>(Inscriptions.getInscriptions().getPersonnes()),
				(element) -> editerPersonne(element));
	}

	private Menu editerPersonne(Personne personne) {
		Menu menu = new Menu("Editer " + personne.getNom());
		menu.add(editPersonne(personne));
		menu.add(deletePersonne(personne));
		menu.addBack("q");
		return menu;
	}

	private Option editPersonne(Personne personne) {
		return new Option("Modifier", "a", () -> {

			personne.setNom(getString("Nouveau nom : \n"));
			personne.setPrenom(getString("Nouveau prenom : \n"));
			personne.setMail(getString("Nouveau mail : \n"));

		});
	}

	private Option deletePersonne(Personne personne) {
		return new Option("Supprimer", "b", () -> {
			personne.delete();
		});
	}

	private Menu menuEquipe() {
		Menu menu = new Menu("Gestion des equipes", "e");

		menu.add(listEquipe());

		menu.add(addEquipe());

		menu.add(selectEquipe());

		menu.addBack("b");
		menu.setAutoBack(true);
		return menu;
	}

	private Option listEquipe() {
		return new Option("Liste des �quipes", "l", () -> {
			System.out.println(Inscriptions.getInscriptions().getEquipes());
		});

	}

	private Option addEquipe() {
		return new Option("Ajouter une nouvelle �quipe", "a", () -> {
			Inscriptions.getInscriptions().createEquipe(getString("Nom de l'�quipe : \n"));
		});
	}

	private List<Equipe> selectEquipe() {
		return new List<Equipe>("S�lectionner une �quipe", "e",
				() -> new ArrayList<>(Inscriptions.getInscriptions().getEquipes()), (element) -> editerEquipe(element));
	}

	private Menu editerEquipe(Equipe equipe) {
		Menu menu = new Menu("Editer " + equipe.getNom());
		menu.add(listMembres(equipe));
		menu.add(addMembre(equipe));
		menu.add(deleteMembre(equipe));
		menu.add(deleteEquipe(equipe));
		menu.addBack("q");
		menu.setAutoBack(true);
		return menu;
	}

	private Option listMembres(Equipe equipe) {
		return new Option("Afficher l'�quipe", "a", () -> {
			System.out.println(equipe.getMembres());
		});
	}

	private List<Personne> addMembre(Equipe equipe) {
		return new List<>("Ajouter un membre", "m",
				() -> new ArrayList<>(Inscriptions.getInscriptions().getPersonnes()), (index, element) -> {
					equipe.add(element);
				});
	}

	private List<Personne> deleteMembre(Equipe equipe) {
		return new List<>("Supprimer un membre", "s", () -> new ArrayList<>(equipe.getMembres()), (index, element) -> {
			equipe.remove(element);
		});
	}

	private Option deleteEquipe(Equipe equipe) {
		return new Option("Supprimer l'�quipe", "d", () -> {
			equipe.delete();
		});
	}

	public static void main(String[] args) {
		HibernateFactory.buildIfNeeded();
		mainUser menu = new mainUser();
		menu.start();
	}
}
