package com.diduweiwu.base;

import com.diduweiwu.exception.EntityNotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Objects;

/**
 * @author itest
 */
public abstract class BaseService<R extends JpaRepository<E, Long> & JpaSpecificationExecutor<E>, E> {

    @Autowired
    protected R repo;

    public long count() {
        return this.repo.count();
    }

    public long count(Specification<E> specification) {
        return this.repo.count(specification);
    }

    public List<E> list() {
        return this.repo.findAll();
    }

    public List<E> list(Specification<E> specification) {
        return this.repo.findAll(specification);
    }

    public E one(long id) {
        return this.repo.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public E one(Specification<E> specification) {
        return this.repo.findOne(specification).orElseThrow(EntityNotFoundException::new);
    }

    public Page<E> page(BasePageVo pageVo) {
        return this.repo.findAll(PageRequest.of(pageVo.getPageNum(), pageVo.getPageSize()));
    }

    public Page<E> page(Specification<E> specification, BasePageVo pageVo) {
        return this.repo.findAll(specification, PageRequest.of(pageVo.getPageNum(), pageVo.getPageSize()));
    }

    public Page<E> page(Specification<E> specification, int pageNum, int pageSize) {
        return this.repo.findAll(specification, PageRequest.of(pageNum, pageSize));
    }

    /**
     * 根据主键id删除
     *
     * @param id
     * @return
     */
    public E delete(Long id) {
        E entity = this.one(id);
        this.repo.delete(entity);

        return entity;
    }

    /**
     * 根据entity对象删除
     *
     * @param entity
     * @return
     */
    public E delete(E entity) {
        this.repo.delete(entity);
        return entity;
    }

    /**
     * 根据entity对象删除
     *
     * @param entityList
     * @return
     */
    public List<E> deleteAll(List<E> entityList) {
        this.repo.deleteAll(entityList);
        return entityList;
    }

    /**
     * 根据entity对象删除
     *
     * @param ids
     * @return
     */
    public List<Long> deleteAllByIds(List<Long> ids) {
        this.repo.deleteAllByIdInBatch(ids);
        return ids;
    }

    /**
     * 更新/创建
     *
     * @param entity
     * @return
     */
    public E save(E entity) {
        return this.repo.saveAndFlush(entity);
    }

    /**
     * 批量更新/创建
     *
     * @param entityList
     * @return
     */
    public List<E> saveAll(List<E> entityList) {
        return this.repo.saveAllAndFlush(entityList);
    }

    /**
     * 创建
     *
     * @param createVo
     * @return
     */
    @SneakyThrows
    public E create(Object createVo) {
        Objects.requireNonNull(createVo);

        Class<? extends E> eClazz = this.fetchEntityClazz();

        E entity = eClazz.newInstance();
        BeanUtils.copyProperties(createVo, entity);

        return this.repo.saveAndFlush(entity);
    }

    /**
     * 更新
     *
     * @param createVo
     * @return
     */
    @SneakyThrows
    public E save(Long id, Object createVo) {
        E entity = this.one(id);
        BeanUtils.copyProperties(createVo, entity);
        return this.repo.saveAndFlush(entity);
    }

    /**
     * 反射必须要使用clazz对象,但是无法通过类型符E调用初始化函数,所以此处只能从实现类返回
     *
     * @return
     */
    public abstract Class<? extends E> fetchEntityClazz();
}
