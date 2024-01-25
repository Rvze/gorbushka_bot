package database

import (
	"context"
	"corenotif/log"
	"corenotif/model/entity"
	"fmt"

	"github.com/jackc/pgx/v4/pgxpool"
)

type Repository interface {
	Insert(notification *entity.Notification) error
	GetAllByModelId(modelId string) ([]entity.NotificationSubscription, error)
}

type NotificationSubscriptionRepository struct {
	pool *pgxpool.Pool
}

func NewRepository(pool *pgxpool.Pool) Repository {
	defer func() {
		log.Log(log.SUCCESS, "NotificationSubscriptionRepository set")
	}()

	return &NotificationSubscriptionRepository{
		pool: pool,
	}
}

func (r *NotificationSubscriptionRepository) Insert(notification *entity.Notification) error {
	conn, err := r.pool.Acquire(context.Background())
	if err != nil {
		return err
	}

	defer conn.Release()

	_, err = conn.Exec(
		context.Background(),
		`
			INSERT INTO notification (user_id, subscription_id, text, status, read_at, created_at)
			VALUES ($1, $2, $3, $4, $5, $6) ON CONFLICT DO NOTHING;
		`,
		notification.UserId,
		notification.SubscriptionId,
		notification.Text,
		notification.Status,
		notification.ReadAt,
		notification.CreatedAt,
	)
	if err != nil {
		fmt.Println(log.Err("Error while inserting", err))
	}

	return err
}

func (r *NotificationSubscriptionRepository) GetAllByModelId(modelId string) ([]entity.NotificationSubscription, error) {
	conn, err := r.pool.Acquire(context.Background())
	if err != nil {
		return nil, err
	}

	defer conn.Release()

	sql := `
		SELECT *
		  FROM notification_subscription ns
		 WHERE ns.entity_id = $1;
   `

	rows, err := conn.Query(context.Background(), sql, modelId)
	if err != nil {
		fmt.Println(log.Err("Error while selecting", err))
		return nil, err
	}
	log.Log(log.INFO, fmt.Sprintf("Exectutet sql - %s entity id - %s", sql, modelId))

	defer rows.Close()

	subscriptions := make([]entity.NotificationSubscription, 0)
	for rows.Next() {
		var ns entity.NotificationSubscription
		if err := rows.Scan(&ns.Id, &ns.UserId, &ns.ModelId, &ns.SubscriptionType); err != nil {
			fmt.Println(log.Err("Error while scanning rows", err))
			return nil, err
		}
		subscriptions = append(subscriptions, ns)
	}

	if err := rows.Err(); err != nil {
		fmt.Println(log.Err("Error in rows", err))
		return nil, err
	}

	return subscriptions, nil
}
