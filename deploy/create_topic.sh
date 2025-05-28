/opt/bitnami/kafka/bin/kafka-topics.sh \
  --create \
  --bootstrap-server localhost:9092 \
  --replication-factor 1 \
  --partitions 2 \
  --topic public.customer-costs.v1

echo "Topic 'public.customer-costs.v1' crated."

/opt/bitnami/kafka/bin/kafka-topics.sh \
  --create \
  --bootstrap-server clocalhost:9092 \
  --replication-factor 1 \
  --partitions 2 \
  --topic public.costs-category.v1

echo "Topic 'public.costs-category.v1' crated."