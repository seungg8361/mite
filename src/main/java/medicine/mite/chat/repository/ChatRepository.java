package medicine.mite.chat.repository;

import medicine.mite.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c WHERE c.keyword = :keyword")
    Optional<Chat> findByKeyword(@Param("keyword") String keyword);
}

