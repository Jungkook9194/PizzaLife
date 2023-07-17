package PizzaLife.repository;

import PizzaLife.entity.gestion.CategoriaEntity;
import PizzaLife.entity.gestion.ProductosEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<ProductosEntity, Long>{
    @Query("select p from ProductoEntity p where p.estado=1")    
    List<ProductosEntity> findAllCustom();
    List<ProductosEntity> findByCategoria(CategoriaEntity categoria);
}
