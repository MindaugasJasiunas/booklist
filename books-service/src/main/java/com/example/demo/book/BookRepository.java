package com.example.demo.book;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
    Flux<Book> findAllBy(Pageable pageable);

    Mono<Book> findByISBN(String ISBN);

    Mono<Void> deleteByISBN(String ISBN);

//    @Query("""
//            db.books.find(
//                {$or:[
//                    {"author":{ $regex:".*?0.*"}},
//                    {"title": { $regex:".*?0.*"}},
//                ]}
//            )
//            """)
    @Query("{$or:[ { 'author' : { $regex: '.*?0.*' } }, { 'title' : { $regex: '.*?0.*' } } ]}")
    Flux<Book> findAllBySearch(String searchText);

//    @Query("""
//            db.books.aggregate([
//                { $match : {$or:[
//                            {'author' : { $regex: '.*?0.*'}},
//                            {'title' : { $regex: '.*?0.*'}},
//                        ]} },
//                { $skip : ?1 },
//                { $limit: ?2 }
//            ]);
//            """)
    @Aggregation(pipeline = {
            "{$match : {$or:[ {'author' : { $regex: '.*?0.*'} }, {'title' : { $regex: '.*?0.*'} } ]} }",
            "{ $skip : ?1 }",
            "{ $limit: ?2 }"
    })
    Flux<Book> findAllBySearchAndPaginate(String searchText, int skip, int limit);

}
