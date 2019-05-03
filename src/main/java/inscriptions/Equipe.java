package inscriptions;

import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 * Représente une Equipe. C'est-à-dire un ensemble de personnes pouvant 
 * s'inscrire à une compétition.
 * 
 */

@Entity(name ="Equipe")
public class Equipe extends Candidat
{
	private static final long serialVersionUID = 4147819927233466035L;
	
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "equipe_personne",
		joinColumns = { @JoinColumn(name = "fk_equipe") },
		inverseJoinColumns = { @JoinColumn(name = "fk_personne") })
	private Set<Personne> membres = new TreeSet<>();
	
	public Equipe() {
		
	}
	Equipe(Inscriptions inscriptions, String nom)
	{
		super(inscriptions, nom);
	}

	/**
	 * Retourne l'ensemble des personnes formant l'équipe.
	 */
	
	public SortedSet<Personne> getMembres()
	{
		return Collections.unmodifiableSortedSet((TreeSet<Personne>)membres);
	}
	
	/**
	 * Ajoute une personne dans l'équipe.
	 * @param membre
	 * @return
	 */

	public boolean add(Personne membre)
	{
		membre.add(this);
		return membres.add(membre);
	}

	/**
	 * Supprime une personne de l'équipe. 
	 * @param membre
	 * @return
	 */
	
	public boolean remove(Personne membre)
	{
		membre.remove(this);
		return membres.remove(membre);
	}

	/**
	 * Retourne les personnes que l'on peut ajouter dans cette équipe.
	 * @return les personnes que l'on peut ajouter dans cette équipe.
	 */
	
	public Set<Personne> getPersonnesAAjouter()
	{
		Set<Personne> listeDesPersonnesDisponible = new TreeSet<>();
		
		for (Personne personne : Inscriptions.getInscriptions().getPersonnes()) {
			if (!getMembres().contains(personne)) {
				listeDesPersonnesDisponible.add(personne);				
			}
		}
		
		return listeDesPersonnesDisponible;
	}
	
	@Override
	public void delete()
	{
		super.delete();
	}
	
	@Override
	public String toString()
	{
		return "Equipe " + super.toString();
	}
}
