// src/pages/AnalyticsDetail.jsx
import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Container, Row, Col, Card, Table, Button } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import { Doughnut } from 'react-chartjs-2';
import './AnalyticsDetail.css';
import {logout} from "../auth";

ChartJS.register(ArcElement, Tooltip, Legend);

export default function AnalyticsDetail() {
    const navigate = useNavigate();

    // Mock data
    const [data] = useState({
        total: -54431.32,
        categories: [
            { id: 1, desc: 'Transfers',            tx: 4,  amount: -21105.32, percent: 39, color: '#6f42c1' },
            { id: 2, desc: 'Digital Goods',        tx: 5,  amount: -7679.00,  percent: 14, color: '#fd7e14' },
            { id: 3, desc: 'Online Stores',        tx: 1,  amount: -7177.00,  percent: 13, color: '#6610f2' },
            { id: 4, desc: 'Taxi',                 tx: 37, amount: -5554.00,  percent: 10, color: '#007bff' },
            { id: 5, desc: 'Budget Payments',      tx: 1,  amount: -3472.00,  percent: 6,  color: '#20c997' },
            { id: 6, desc: 'Beauty',               tx: 3,  amount: -3149.00,  percent: 6,  color: '#e83e8c' },
            // …more categories
        ]
    });

    // Chart dataset
    const chartData = {
        labels: data.categories.map(c => c.desc),
        datasets: [{
            data: data.categories.map(c => Math.abs(c.amount)),
            backgroundColor: data.categories.map(c => c.color),
            hoverOffset: 8,
            cutout: '70%'
        }]
    };

    // Center label state
    const [center, setCenter] = useState(data.categories[0]);

    const options = {
        onClick: (evt, elements) => {
            if (elements.length) {
                const idx = elements[0].index;
                setCenter(data.categories[idx]);
            }
        },
        plugins: {
            tooltip: {
                callbacks: {
                    label: ctx =>
                        `${data.categories[ctx.dataIndex].desc}: ₴${Math.abs(ctx.raw).toLocaleString()} (${data.categories[ctx.dataIndex].percent}%)`
                }
            },
            legend: { display: false }
        }
    };

    const handleLogout = () => {
        logout();
        navigate('/login');
    };


    return (
        <div className="container py-lg-3 position-relative flex-grow-1">
            <header className="mb-4 text-center position-relative">
                <button
                    className="btn btn-outline-secondary position-absolute"
                    style={{ top: '1rem', left: '1rem' }}
                    onClick={() => navigate('/')}>
                    Home
                </button>

                <h1 className="display-6 text-primary">CoinKeeper</h1>

                <button
                    className="btn btn-outline-secondary position-absolute"
                    style={{ top: '1rem', right: '1rem' }}
                    onClick={handleLogout}>
                    Logout
                </button>
            </header>

            <Container fluid className="py-4 analytics-detail">
                <div className="d-flex align-items-center mb-4">
                    <h2 className="m-0">Expenses Detail</h2>
                </div>
                <Row>
                    {/* Donut chart */}
                    <Col md={6}>
                        <Card className="shadow-sm">
                            <Card.Body className="position-relative text-center p-5">
                                <div className="doughnut-wrapper">
                                    <Doughnut data={chartData} options={options} />
                                    <div className="doughnut-center">
                                        <div className="fw-bold">{center.desc}</div>
                                        <div className="h4 my-1">₴{Math.abs(center.amount).toLocaleString()}</div>
                                        <div className="text-secondary">{center.percent}%</div>
                                    </div>
                                </div>
                                <div className="mt-3">
                                    Total for May<br/>
                                    <span className="h3 text-danger">₴{data.total.toLocaleString()}</span>
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>

                    {/* Categories table */}
                    <Col md={6}>
                        <Card className="shadow-sm">
                            <Card.Body>
                                <Table hover striped className="mb-0">
                                    <thead className="table-light">
                                    <tr>
                                        <th>Category</th>
                                        <th>Transactions</th>
                                        <th className="text-end">Amount</th>
                                        <th className="text-end">Spend %</th>
                                        <th />
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {data.categories.map(cat => (
                                        <tr key={cat.id} className="align-middle">
                                            <td>
                                                <div className="d-flex align-items-center">
                                                    <div
                                                        className="category-icon me-2"
                                                        style={{ backgroundColor: cat.color }}
                                                    >
                                                        <i className="bi bi-arrow-right-short text-white"></i>
                                                    </div>
                                                    {cat.desc}
                                                </div>
                                            </td>
                                            <td>{cat.tx}</td>
                                            <td className="text-end">₴{Math.abs(cat.amount).toLocaleString()}</td>
                                            <td className="text-end">{cat.percent}%</td>
                                            <td className="text-end">
                                                <Button variant="link" className="p-0">
                                                    <i className="bi bi-chevron-right"></i>
                                                </Button>
                                            </td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </Table>
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

