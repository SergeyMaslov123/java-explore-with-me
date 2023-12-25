package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Likes", schema = "public")
public class Like {
    @EmbeddedId
    private LikePk id;
    @Column(name = "like_event")
    private Boolean likeEvent;
}
