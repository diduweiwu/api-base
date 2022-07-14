# api-base

Spring接口快速开发的一些Base类,快速生成常用接口和方法,避免很多雷同代码

```text
例如,需要针对Book开发一套Crud接口操作,就需要在Controller中写一堆get,put,post,delete接口,
然后还需要在Service里面写一堆对应的Repo调用,十分繁琐,而且容易造成代码不一致.
那么,我们可以将这些模板化的接口代码抽离出来放到基类,这样针对对应实体,即可快速生成一套对应且统一的接口.
```

## 使用方法

### Entity 配置

```text
Entity是与业务对象相关的,里面配置了对应的字段还有模型关联关系,这个无法抽象,只能根据各自业务数据库自行实现.
假设我们的Entity实体对象叫DemoEntity
```

### Repo 配置

```text
Repo是个接口,需要继承 JpaRepository<E, Long> , JpaSpecificationExecutor<E> 这两个 接口,
因为后续Service层会用到对应的方法
假设我们的Repo接口叫DemoRepo
```
```java
public interface DemoRepo extends JpaRepository<E, Long> , JpaSpecificationExecutor<E> {

}
```

### Service 配置

很多逻辑都封装在了BaseService逻辑里面,所以我们的Service需要继承BaseService并传入Repo和Entity泛型
如下

```java

public class DemoService extends BaseService<DemoRepo,DemoEntity>{
}
PS:如果需要create和save方法的话,需要在子类实现 fetchEntityClazz 函数返回Entity的class对象
```

### Controller配置

```text
Controller对应API接口,BaseController中的API接口函数已经做到与BaseService中一致,
所以直接继承BaseController并传入对应泛型参数即可
```

```java
@RestController
@RequestMapping(path = "${prefix}")
public class DemoController extends BaseController<DemoService, DemoRepo, DemoEntity> {

}
PS: 
@RestController 注解是将该类配置为接口类,这个必不可少
@RequestMapping 注解是为一系列接口设置接口路径前缀,否则生成的接口地址会不唯一
```

### 使用

至此,我们已经批量生成了一系列的API接口,下面是接口路径以及对应的函数名称/入参
PS: ${prefix} 替换成你自己配置的接口路径前缀

- ${prefix}/count
    - public ResponseEntity<Long> count()
- ${prefix}/list
    - public ResponseEntity<List<E>> list()
- ${prefix}/one
    - public ResponseEntity<E> one(@RequestParam Long id)
- ${prefix}/page
    - public Page<E> page(BasePageVo pageVo)
- ${prefix}/delete
    - public E delete(@RequestParam Long id)
- ${prefix}/delete-all
    - public List<Long> delete(@RequestParam(name = "idList[]") List<Long> idList)
- ${prefix}/create
    - public E create(@RequestBody Object createVo)
- ${prefix}/update
    - public E update(@RequestParam Long id, @RequestBody Object createVo)

### 小结

如此所有生成的接口路径和函数都能保持一致,对应函数名称一目了然,懒得再写注释了

BaseService函数中也还有很多额外的方法可用,如下所示

- long count()
- long count(Specification<E> specification)
- List<E> list()
- List<E> list(Specification<E> specification)
- E one(long id)
- E one(Specification<E> specification)
- Page<E> page(BasePageVo pageVo)
- Page<E> page(Specification<E> specification, BasePageVo pageVo)
- Page<E> page(Specification<E> specification, int pageNum, int pageSize)
- E delete(Long id)
- E delete(E entity)
- List<E> deleteAll(List<E> entityList)
- List<Long> deleteAllByIds(List<Long> ids)
- E save(E entity)
- List<E> saveAll(List<E> entityList)
- E create(Object createVo)
- E save(Long id, Object createVo)

也可以在Service子类中使用Service基类中的repo对象(protected 访问限定符), 扩展实现更多逻辑操作

由此ApiBase的整体框架初步完成,后续就是扩展实现更多统一的接口方法
