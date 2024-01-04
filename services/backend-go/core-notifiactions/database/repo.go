package database

import (
	"context"
	"corenotif/log"
	"corenotif/model/entity"
	"fmt"

	"github.com/jackc/pgx/v4/pgxpool"
)

type Repository interface {
	Insert(chatId int, modelType entity.ModelType, modelId string) error
	GetAllByModelTypeAndModelId(modelType entity.ModelType, modelId string) ([]int, error)
	Delete(chatId int) error
}

type repository struct {
	pool *pgxpool.Pool
}

func NewRepository(pool *pgxpool.Pool) Repository {
	defer func() {
		log.Log(log.SUCCESS, "repository set")
	}()
	return &repository{
		pool: pool,
	}
}

func (r *repository) Insert(chatId int, modelType entity.ModelType, modelId string) error {
	conn, err := r.pool.Acquire(context.Background())
	if err != nil {
		return err
	}
	defer conn.Release()

	_, err = conn.Exec(context.Background(), `
		INSERT INTO subscription_notify (tg_chat_id, model_id, model_type)
		VALUES ($1, $2, $3)
	`, chatId, modelId, modelType)
	if err != nil {
		fmt.Println(log.Err("Error while inserting", err))
	}
	return err
}

func (r *repository) GetAllByModelTypeAndModelId(modelType entity.ModelType, modelId string) ([]int, error) {
	conn, err := r.pool.Acquire(context.Background())
	if err != nil {
		return nil, err
	}

	defer conn.Release()
	sql := `
	SELECT t.tg_chat_id
	  FROM subscription_notify t
	 WHERE t.model_type = $1
	   AND t.model_id = $2`

	rows, err := conn.Query(context.Background(), sql, modelType, modelId)
	if err != nil {
		fmt.Println(log.Err("Error while selecting", err))
		return nil, err
	}
	defer rows.Close()

	chatIdSlice := make([]int, 0)
	for rows.Next() {
		var tgChatId int
		if err := rows.Scan(&tgChatId); err != nil {
			fmt.Println(log.Err("Error while scanning rows", err))
			return nil, err
		}
		chatIdSlice = append(chatIdSlice, tgChatId)
	}

	if err := rows.Err(); err != nil {
		fmt.Println(log.Err("Error in rows", err))
		return nil, err
	}

	return chatIdSlice, nil
}

func (r *repository) Delete(chatId int) error {

	conn, err := r.pool.Acquire(context.Background())
	if err != nil {
		return err
	}

	defer conn.Release()

	sql := "DELETE FROM subscription_notify WHERE tg_chat_id=$1"

	_, err = conn.Exec(context.Background(), sql, chatId)
	if err != nil {
		return err
	}

	return err
}
