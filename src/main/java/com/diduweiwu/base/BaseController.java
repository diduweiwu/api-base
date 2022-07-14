package com.diduweiwu.base;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author itest
 */
public abstract class BaseController<S extends BaseService<R, E>, R extends JpaRepository<E, Long> & JpaSpecificationExecutor<E>, E> {

    @Resource
    protected S service;

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(this.service.count());
    }

    @GetMapping("/list")
    public ResponseEntity<List<E>> list() {
        return ResponseEntity.ok(this.service.list());
    }

    @GetMapping("/one")
    public ResponseEntity<E> one(@RequestParam Long id) {
        return ResponseEntity.ok(this.service.one(id));
    }

    @GetMapping("/page")
    public Page<E> page(BasePageVo pageVo) {
        return this.service.page(pageVo);
    }

    @DeleteMapping("/delete")
    public E delete(@RequestParam Long id) {
        return this.service.delete(id);
    }

    @DeleteMapping("/delete-all")
    public List<Long> delete(@RequestParam(name = "idList[]") List<Long> idList) {
        return this.service.deleteAllByIds(idList);
    }

    @PostMapping("/create")
    public E create(@RequestBody Object createVo) {
        return this.service.create(createVo);
    }

    @PutMapping("/update")
    public E update(@RequestParam Long id, @RequestBody Object createVo) {
        return this.service.save(id, createVo);
    }
}
