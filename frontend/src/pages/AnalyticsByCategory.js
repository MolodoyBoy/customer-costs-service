// src/pages/AnalyticsByCategory.jsx
import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import {
    Container,
    Row,
    Col,
    Card,
    Table,
    Button,
    Pagination
} from 'react-bootstrap';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import {
    Chart as ChartJS,
    LineElement,
    CategoryScale,
    LinearScale,
    PointElement,
    Tooltip,
    Legend
} from 'chart.js';
import { Line } from 'react-chartjs-2';
import {
    ApiClient,
    CategorizedCostsAnalyticsApi
} from 'ccs-openapi-client';
import { getAuthToken, logout } from '../auth';
import './AnalyticsByCategory.css';

ChartJS.register(
    LineElement,
    CategoryScale,
    LinearScale,
    PointElement,
    Tooltip,
    Legend
);

export default function AnalyticsByCategory() {
    const navigate = useNavigate();
    const { state } = useLocation();
    const analyticsCategoryId = state?.analyticsCategoryId;

    const [summary, setSummary] = useState({
        amount: 0,
        average: 0,
        categoryDescription: ''
    });

    const [customerCosts, setCustomerCosts] = useState([]);
    const [extrapolatedCustomerCosts, setExtrapolatedCustomerCosts] = useState([]);

    const [page, setPage] = useState(1);
    const pageSize = 10;

    // Перезагружаем при изменении категории или номера страницы
    useEffect(() => {
        if (!analyticsCategoryId) return;

        const token = getAuthToken();
        if (token) {
            ApiClient.instance.defaultHeaders = {
                ...ApiClient.instance.defaultHeaders,
                Authorization: token
            };
        }

        new CategorizedCostsAnalyticsApi().getCategorizedCostsAnalytics(
            analyticsCategoryId,
            { page, pageSize },
            (err, resp) => {
                if (err) {
                    console.error(err);
                    return;
                }

                const cat = resp.categorizedCostsAnalytics;
                setSummary({
                    amount: cat.amount,
                    average: cat.average,
                    categoryDescription: cat.categoryDescription
                });

                // resp.extrapolated: array of { amount, description, createdAt }
                setExtrapolatedCustomerCosts(resp.extrapolated || []);

                // resp.customerCosts: array of { amount, description, createdAt }
                setCustomerCosts(resp.customerCosts || []);
            }
        );
    }, [analyticsCategoryId, page]);

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    // chart data из текущей страницы customerCosts
    const chartData = {
        labels: extrapolatedCustomerCosts.map(c =>
            new Date(c.createdAt).toLocaleDateString()
        ),
        datasets: [
            {
                data: extrapolatedCustomerCosts.map(c => c.amount),
                fill: false,
                tension: 0.3,
                borderColor: '#007bff',
                pointBackgroundColor: '#007bff',
                borderWidth: 3
            }
        ]
    };

    const chartOptions = {
        scales: {
            y: { ticks: { callback: v => `₴${v}` } },
            x: {}
        },
        plugins: {
            legend: { display: false },
            tooltip: { callbacks: { label: ctx => `₴${ctx.parsed.y.toLocaleString()}` } }
        },
        maintainAspectRatio: false
    };

    return (
        <div className="container py-lg-3 position-relative">
            {/* Header */}
            <header className="mb-4 text-center position-relative">
                {/* Левая кнопка */}
                <button
                    className="btn btn-outline-secondary position-absolute"
                    style={{ top: '1rem', left: '1rem' }}
                    onClick={() => navigate('/')}>
                    Home
                </button>

                {/* Заголовок по-центру */}
                <h1 className="display-6 text-primary">CoinKeeper</h1>

                {/* Правая кнопка */}
                <button
                    className="btn btn-outline-secondary position-absolute"
                    style={{ top: '1rem', right: '1rem' }}
                    onClick={handleLogout}>
                    Logout
                </button>
            </header>

            <Container fluid className="py-4 analytics-by-category">
                <Row className="align-items-center mb-4">
                    <Col>
                        <h2 className="mb-0">{summary.categoryDescription}</h2>
                    </Col>
                </Row>

                <Row className="mb-5">
                    {/* Summary */}
                    <Col lg={4}>
                        <Card className="p-3 h-100 shadow-sm text-center">
                            <h5 className="text-muted">Total Expenses</h5>
                            <h2 className="my-3 text-primary">
                                ₴{summary.amount.toLocaleString()}
                            </h2>
                            <div className="mt-4 d-flex justify-content-between align-items-center">
                                <h5 className="text-muted mb-0">Average expenses:</h5>
                                <h5 className="text-primary fw-semibold mb-0">
                                    ₴{summary.average.toLocaleString()}
                                </h5>
                            </div>
                        </Card>
                    </Col>

                    {/* Chart */}
                    <Col lg={8}>
                        <Card className="shadow-sm">
                            <Card.Body style={{ height: '300px' }}>
                                <Line data={chartData} options={chartOptions} />
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>

                {/* Transactions table */}
                <Row>
                    <Col>
                        <Card className="shadow-sm">
                            <Card.Header as="h5">Expenses</Card.Header>
                            <Card.Body className="p-0">
                                <Table hover striped className="mb-0">
                                    <thead className="table-light">
                                    <tr>
                                        <th>Date</th>
                                        <th>Description</th>
                                        <th className="text-end">Amount</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {customerCosts.length > 0 ? (
                                        customerCosts.map(tx => (
                                            <tr key={tx.createdAt + tx.amount}>
                                                <td>{new Date(tx.createdAt).toLocaleDateString()}</td>
                                                <td>{tx.description}</td>
                                                <td className="text-end">
                                                    ₴{Math.abs(tx.amount).toLocaleString()}
                                                </td>
                                            </tr>
                                        ))
                                    ) : (
                                        <tr className="table-light">
                                            <td colSpan={3} className="text-center text-muted">
                                                No transactions
                                            </td>
                                        </tr>
                                    )}
                                    </tbody>
                                </Table>

                                {/* Pagination */}
                                <Pagination className="m-3 justify-content-center">
                                    <Pagination.Prev
                                        disabled={page === 1}
                                        onClick={() => setPage(p => Math.max(1, p - 1))}
                                    />
                                    <Pagination.Item active>{page}</Pagination.Item>
                                    <Pagination.Next
                                        disabled={customerCosts.length < pageSize}
                                        onClick={() => setPage(p => p + 1)}
                                    />
                                </Pagination>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>

            {/* Footer */}
            <footer className="text-center text-muted small">
                © {new Date().getFullYear()} CoinKeeper
            </footer>
        </div>
    );
}

