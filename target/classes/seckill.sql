-- 秒杀执行存储过程
-- 存储过程
-- 1.存储过程优化：让事务行级锁所持有的时间尽可能的短
-- 2.不要过度依赖存储过程
-- 3.简单的逻辑可以应用存储过程
-- 4.QPS:一个秒杀单6000/qps

-- MySQL默认以";"为分隔符，如果没有声明分割符，则编译器会把存储过程当成SQL语句进行处理，因此编译过程会报错，
-- 所以要事先用“DELIMITER //”声明当前段分隔符，让编译器把两个"//"之间的内容当做存储过程的代码，不会执行这些代码；
-- “DELIMITER ;”的意为把分隔符还原。

-- 秒杀存储执行过程  console ; 转换为 $$

DELIMITER $$
-- 定义存储过程
-- 参数in输入参数，out输出参数
-- row_count()：返回上亿条修改类型sql(delete,insert,update)影响行数
-- row_count:0未修改数据 >0表示修改的行数，<0代表sql错误或者未修改sql
CREATE PROCEDURE `seckill`.`execute_seckill` (IN v_seckill_id BIGINT,IN v_phone BIGINT, IN v_kill_time TIMESTAMP ,OUT r_result INT )
  BEGIN
    DECLARE insert_count INT DEFAULT 0;
    START TRANSACTION ;
    INSERT IGNORE INTO success_killed
    (seckill_id,user_phone,create_time) VALUES (v_seckill_id,v_phone,v_kill_time);
    SELECT ROW_COUNT() INTO insert_count;
    IF(insert_count = 0) THEN
      ROLLBACK ;
      SET r_result = -1;
    ELSEIF(insert_count < 0) THEN
      ROLLBACK ;
      SET r_result = -2;
    ELSE
      UPDATE seckill SET NUMBER = NUMBER -1
      WHERE seckill_id = v_seckill_id AND end_time>v_kill_time AND start_time<v_kill_time AND NUMBER >0;
      SELECT ROW_COUNT() INTO insert_count;
      IF(insert_count = 0) THEN
        ROLLBACK ;
        SET r_result = 0;
      ELSEIF(insert_count<0) THEN
        ROLLBACK ;
        SET r_result = -2;
      ELSE
        COMMIT ;
        SET r_result = 1;
      END IF;
    END IF;
  END;
$$
-- 存储过程定义结束

-- 验证存储过程是否有错
DELIMITER ;
SET @r_result=-3;
-- 执行存储过程
-- bug: Unknown column 'row_count' in 'field list'
call execute_seckill(1002,32345678123,now(),@r_result);
-- 获取结果
SELECT @r_result;




