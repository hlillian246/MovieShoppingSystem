use cs122b_db43;
delimiter //
drop procedure if exists insert_sales_transactions //
create procedure insert_sales_transactions
(
    IN email varchar(50),
    IN movieId varchar(50),
    IN quantity int,
    IN token varchar(50)
)
begin
    DECLARE lastSId int;
    INSERT INTO sales(email, movieId, quantity, saleDate) VALUES (email, movieId, quantity, curdate());
    set lastSId = last_insert_id();
    INSERT INTO transactions(sId, token) VALUES (lastSId, token);
end;
//
delimiter ;

