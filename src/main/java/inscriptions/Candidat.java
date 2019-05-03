package inscriptions;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Candidat à un événement sportif, soit une personne physique, soit une équipe.
 *
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Candidat")
public abstract class Candidat implements Comparable<Candidat>, Serializable
{
	private static final long serialVersionUID = -6035399822298694746L;

	@Transient
	private Inscriptions inscriptions;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

	private int id;
	
	private String nom;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "inscriptions",
		joinColumns = { @JoinColumn(name = "fk_candidat") },
		inverseJoinColumns = { @JoinColumn(name = "fk_competition") })
	private Set<Competition> competitions = new HashSet<>();
	
	public Candidat() {
		
	}
	public Candidat(Inscriptions inscriptions, String nom)
	{
		this.inscriptions = inscriptions;	
		this.nom = nom;
		competitions = new TreeSet<>();
	}

	/**
	 * Retourne le nom du candidat.
	 * @return
	 */
	
	public String getNom()
	{
		return nom;
	}

	/**
	 * Modifie le nom du candidat.
	 * @param nom
	 */
	
	public void setNom(String nom)
	{
		this.nom = nom;
	}

	/**
	 * Retourne toutes les compétitions auxquelles ce candidat est inscrit.s
	 * @return
	 */

	public Set<Competition> getCompetitions()
	{
		return Collections.unmodifiableSet(competitions);
	}
	
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setCompetitions(Set<Competition> competitions) {
		this.competitions = competitions;
	}
	boolean add(Competition competition)
	{
		return competitions.add(competition);
	}

	boolean remove(Competition competition)
	{
		return competitions.remove(competition);
	}

	/**
	 * Supprime un candidat de l'application.
	 */
	
	public void delete()
	{
		for (Competition c : competitions)
			c.remove(this);
		inscriptions.delete(this);
	}
	
	@Override
	public int compareTo(Candidat o)
	{
		return getNom().compareTo(o.getNom());
	}
	
	@Override
	public String toString()
	{
		return "\n" + getNom() + " -> inscrit à " + getCompetitions();
	}
}
