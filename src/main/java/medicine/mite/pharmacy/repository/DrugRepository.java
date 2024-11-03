package medicine.mite.pharmacy.repository;

import medicine.mite.pharmacy.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DrugRepository extends JpaRepository<Drug, Long> {

    @Query("SELECT d FROM Drug d WHERE d.dname LIKE %:name%")
    List<Drug> findByDrugName(@Param("name") String name);

}