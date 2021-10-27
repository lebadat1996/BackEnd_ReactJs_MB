package com.vsii.enamecard.repositories;

import com.vsii.enamecard.model.dto.PostDTO;
import com.vsii.enamecard.model.entities.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface PostRepository extends JpaRepository<PostEntity, Integer> {
    @Query(value = "select p.id as id ,p.status as status,p.content as content ,p.priority as priority, p.avatar as avatar ,p.title as title , ch.name as nameChannel , c.name as nameCategory from PostEntity p inner join ChannelEntity ch on ch.id = p.channelId inner join CategoryEntity c on c.id = p.categoryId")
    Page<PostDTO.postInfo> listPost(PageRequest pageRequest);
}
