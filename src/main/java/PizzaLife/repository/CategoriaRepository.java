package PizzaLife.repository;

import PizzaLife.entity.gestion.CategoriaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long>{
    //podemos crear query personalizados
    @Query("select c from CategoriaEntity c where c.estado=1")
    List<CategoriaEntity> findAllCustom();
}
