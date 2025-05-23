// src/pages/AnalyticsByCategory.jsx
import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Container, Row, Col, Card, Table, Button, Form } from 'react-bootstrap';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { Chart as ChartJS, LineElement, CategoryScale, LinearScale, PointElement, Tooltip, Legend } from 'chart.js';
import { Line } from 'react-chartjs-2';
import './AnalyticsByCategory.css';

ChartJS.register(LineElement, CategoryScale, LinearScale, PointElement, Tooltip, Legend);

export default function AnalyticsByCategory() {
    const navigate = useNavigate();

    // mock categories
    const allCategories = [
        { id: 1, name: 'Food & Dining' },
        { id: 2, name: 'Transportation' },
        { id: 3, name: 'Shopping' },
    ];
    const [selectedCat, setSelectedCat] = useState(allCategories[0].id);

    // mock time-series data for chart
    const [chartData, setChartData] = useState({ labels: [], datasets: [] });

    // mock table transactions
    const [transactions, setTransactions] = useState([]);

    useEffect(() => {
        // generate mock daily totals for selected category
        const days = Array.from({ length: 7 }, (_, i) => {
            const d = new Date();
            d.setDate(d.getDate() - (6 - i));
            return d.toLocaleDateString('default', { month: 'short', day: 'numeric' });
        });
        const values = days.map(() => (Math.random() * 200).toFixed(2));
        setChartData({
            labels: days,
            datasets: [
                {
                    label: allCategories.find(c => c.id === selectedCat).name,
                    data: values,
                    fill: false,
                    tension: 0.3,
                    borderColor: '#007bff',
                    pointBackgroundColor: '#007bff',
                    borderWidth: 3,
                },
            ],
        });

        // generate mock transactions list
        const txs = Array.from({ length: 5 }, (_, i) => ({
            id: i + 1,
            date: new Date(Date.now() - Math.random() * 7 * 24 * 60 * 60 * 1000)
                .toLocaleDateString(),
            description: `Mock expense #${i + 1}`,
            amount: (-(Math.random() * 100).toFixed(2)),
        }));
        setTransactions(txs);
    }, [selectedCat]);

    const handleLogout = () => {
        // clearAuthToken();
        navigate('/login');
    };

    return (
        <div className="d-flex flex-column min-vh-100">
            {/* Header */}
            <header className="mb-4 text-center position-relative">
                <Link to="/" className="position-absolute" style={{ top: '1rem', left: '1rem' }}>
                    <Button variant="link" className="text-dark p-0">
                        <i className="bi bi-house-fill" style={{ fontSize: '1.5rem' }} />
                    </Button>
                </Link>
                <h1 className="display-6 text-primary">CoinKeeper</h1>
                <button
                    className="btn btn-outline-secondary position-absolute"
                    style={{ top: '1rem', right: '2rem' }}
                    onClick={handleLogout}
                >
                    Logout
                </button>
            </header>

            <Container fluid className="py-4 analytics-by-category">
                <Row className="align-items-center mb-4">
                    <Col xs="auto">
                        <h2 className="mb-0">Analytics by Category</h2>
                    </Col>
                </Row>

                <Row className="mb-5">
                    {/* Total this week */}
                    <Col lg={4}>
                        <Card className="p-3 h-100 shadow-sm text-center">
                            <h5 className="text-muted">Total this week</h5>
                            <h2 className="my-3 text-primary">
                                ₴{chartData.datasets[0]?.data.reduce((sum, v) => sum + Number(v), 0).toFixed(2)}
                            </h2>

                            <div className="mt-4 d-flex justify-content-between align-items-center">
                                <h5 className="text-muted mb-0">Average expenses:</h5>
                                <h5 className="text-primary fw-semibold mb-0">
                                    ₴{(
                                    chartData.datasets[0]?.data.reduce((sum, v) => sum + Number(v), 0) /
                                    chartData.labels.length
                                ).toFixed(2)}
                                </h5>
                            </div>
                        </Card>
                    </Col>

                    {/* Chart */}
                    <Col lg={8}>
                        <Card className="shadow-sm">
                            <Card.Body style={{ height: '300px' }}>
                                <Line data={chartData} options={{
                                    scales: {
                                        y: { ticks: { callback: v => `₴${v}` } },
                                        x: {}
                                    },
                                    plugins: { legend: { display: false }, tooltip: { callbacks: { label: ctx => `₴${ctx.parsed.y}` } } },
                                    maintainAspectRatio: false
                                }} />
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>

                <Row>
                    <Col>
                        <Card className="shadow-sm">
                            <Card.Header as="h5">Recent Transactions</Card.Header>
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
                                    {transactions.map(tx => (
                                        <tr key={tx.id}>
                                            <td>{tx.date}</td>
                                            <td>{tx.description}</td>
                                            <td className="text-end">₴{Math.abs(tx.amount).toLocaleString()}</td>
                                        </tr>
                                    ))}
                                    {transactions.length === 0 && (
                                        <tr className="table-light">
                                            <td colSpan={3} className="text-center text-muted">
                                                No transactions
                                            </td>
                                        </tr>
                                    )}
                                    </tbody>
                                </Table>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>

            {/* Footer */}
            <footer className="mt-auto text-center py-3 bg-white border-top">
                <small className="text-muted">
                    &copy; {new Date().getFullYear()} CoinKeeper. All rights reserved.
                </small>
            </footer>
        </div>
    );
}