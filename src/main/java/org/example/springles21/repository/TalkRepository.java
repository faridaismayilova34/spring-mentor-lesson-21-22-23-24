package org.example.springles21.repository;

import org.example.springles21.enums.TalkStatus;
import org.example.springles21.entity.Talk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TalkRepository extends JpaRepository<Talk, Long>, JpaSpecificationExecutor<Talk> {
    List<Talk> findBySpeakerId(Long speakerId);
    long countBySpeakerId(Long speakerId);

    @Query("SELECT t.room, COUNT(t) FROM Talk t GROUP BY t.room")
    List<Object[]> countTalksPerRoom();

    List<Talk> findByStatus(TalkStatus status);

}




