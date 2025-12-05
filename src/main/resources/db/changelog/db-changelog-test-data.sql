-- --liquibase formatted sql

--changeset artem:1
insert into users(display_name, username, email, password, is_email_verified, image, bio) VALUES (
                                                                                             'acheron',
                                                                                             'acheron',
                                                                                             'zxc@gmail.com',
                                                                                             '$2a$10$zTxD5FadH0c6Nc/5rscDk.kVQXzX.7Ve2la.XM2Rx5r12M.mPvP1C',
                                                                                             true,
                                                                                             'https://lh3.googleusercontent.com/a/ACg8ocKQjF5ZYH6levW5yZb8OzntyNBDvxCRPu8LvLcjG6JQ3Qoc36So=s360-c-no',
                                                                                                  'emo yeah'
                                                                                            );