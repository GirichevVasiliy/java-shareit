package ru.practicum.shareit.comment;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Data
@Entity
@Table(name = "comments", schema = "public")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String text;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    User author;

}
