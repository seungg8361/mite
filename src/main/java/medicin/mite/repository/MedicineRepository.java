package medicin.mite.repository;

import medicin.mite.entity.Medicines;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicines, Long> {
    List<Medicines> findByCid(Long cid);
}