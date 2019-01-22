package eu.lukks.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="room")
public class Room implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long dayPrice;
	private String shortDescription;
	private String description;
	private Integer personNumber;
	
	@OneToMany(mappedBy="room")
	private List<Reservation> reservations;
	
	@OneToMany(mappedBy="room")
	private List<Photo> photos;
	
	
	

}
