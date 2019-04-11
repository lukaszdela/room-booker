package eu.lukks.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {
	
	private Long id;
	private String title;
	private Long dayPrice;
	private String shortDescription;
	private String description;
	private Integer personNumber;
	private Boolean status;
	private String mainPhotoThumb;
	private String mainPhoto;

}
