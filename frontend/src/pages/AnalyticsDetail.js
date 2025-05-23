// src/pages/AnalyticsDetail.jsx
import React, {useState, useEffect} from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Container, Row, Col, Card, Table, Button} from 'react-bootstrap';
import {Link, useNavigate, useLocation} from 'react-router-dom';
import {Chart as ChartJS, ArcElement, Tooltip, Legend} from 'chart.js';
import {Doughnut} from 'react-chartjs-2';
import {ApiClient, PeriodCostsAnalyticsApi} from 'ccs-openapi-client';
import {getAuthToken, logout} from '../auth';
import './AnalyticsDetail.css';

ChartJS.register(ArcElement, Tooltip, Legend);

export default function AnalyticsDetail() {
    const navigate = useNavigate();
    const {state} = useLocation();
    const periodId = state?.periodId;

    const [categories, setCategories] = useState([]);
    const [total, setTotal] = useState(0);
    const [center, setCenter] = useState(null);

    const categoryColorMap = {
        1: '#6f42c1', // purple
        2: '#fd7e14', // orange
        3: '#6610f2', // indigo
        4: '#007bff', // blue
        5: '#20c997', // teal
        6: '#e83e8c', // pink
        7: '#dc3545', // danger-red
        8: '#198754', // success-green
        9: '#0dcaf0', // cyan
        10: '#ffc107'  // warning-yellow
    };

    useEffect(() => {
        const token = getAuthToken();
        if (token) {
            ApiClient.instance.defaultHeaders = {
                ...ApiClient.instance.defaultHeaders,
                Authorization: token,
            };
        }
        if (!periodId) return;
        new PeriodCostsAnalyticsApi().getPeriodCostsAnalytics(
            periodId,
            null,
            (err, resp) => {
                const arr = resp.categorizedCostsAnalytics || [];
                setCategories(arr);
                setTotal(resp.periodCostsAnalytics.amount);
                setCenter(arr[0] || null);
            }
        );
    }, [periodId]);

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    const chartData = {
        labels: categories.map(c => c.categoryDescription),
        datasets: [{
            data: categories.map(c => Math.abs(c.amount)),
            backgroundColor: categories.map(c => categoryColorMap[c.categoryId]),
            hoverOffset: 6,
            cutout: '60%'
        }]
    };

    const options = {
        onClick: (evt, elems) => {
            if (elems.length) {
                setCenter(categories[elems[0].index]);
            }
        },
        plugins: {
            legend: {display: false},
            tooltip: {
                callbacks: {
                    label: ctx => {
                        const c = categories[ctx.dataIndex];
                        return `${c.categoryDescription}: ₴${Math.abs(c.amount).toLocaleString()}`;
                    }
                }
            }
        },
        maintainAspectRatio: false
    };

    return (
        <div className="container py-lg-3 position-relative flex-grow-1">
            <header className="mb-4 text-center position-relative">
                <button
                    className="btn btn-outline-secondary position-absolute"
                    style={{top: '1rem', left: '1rem'}}
                    onClick={() => navigate('/')}>
                    Home
                </button>

                <h1 className="display-6 text-primary">CoinKeeper</h1>

                <button
                    className="btn btn-outline-secondary position-absolute"
                    style={{top: '1rem', right: '1rem'}}
                    onClick={handleLogout}>
                    Logout
                </button>
            </header>

            <Container fluid className="py-4 analytics-detail">
                <h2 className="mb-4">Expenses Detail</h2>
                <Row>
                    {/* Donut Chart */}
                    <Col md={6}>
                        <Card className="shadow-sm">
                            <Card.Body className="position-relative text-center p-5" style={{padding: '3rem'}}>
                                <div className="doughnut-wrapper">
                                    <Doughnut data={chartData} options={options}/>
                                    {center && (
                                        <div className="doughnut-center">
                                            <div className="fw-bold">{center.categoryDescription}</div>
                                            <div className="h5 my-1">₴{Math.abs(center.amount).toLocaleString()}</div>
                                        </div>
                                    )}
                                </div>
                                <div className="mt-3">
                                    Total<br/>
                                    <span className="h4 text-primary">₴{total.toLocaleString()}</span>
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>

                    {/* Expense Categories Table */}
                    <Col md={6}>
                        <Card className="shadow-sm">
                            <Card.Body>
                                <Table hover striped className="mb-0">
                                    <thead className="table-light">
                                    <tr>
                                        <th style={{width: '1rem'}}></th>
                                        <th>Category</th>
                                        <th>Transactions</th>
                                        <th className="text-end">Amount</th>
                                        <th className="text-end">Percent</th>
                                        <th></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {categories.length > 0 ? categories.map(cat => (
                                        <tr key={cat.id} className="align-middle">
                                            <td>
                                                <span
                                                    style={{
                                                        display: 'inline-block',
                                                        width: '12px',
                                                        height: '12px',
                                                        borderRadius: '50%',
                                                        backgroundColor: categoryColorMap[cat.categoryId] || '#ccc'
                                                    }}
                                                />
                                            </td>
                                            <td>{cat.categoryDescription}</td>
                                            <td>{cat.transactionsCount}</td>
                                            <td className="text-end">₴{Math.abs(cat.amount).toLocaleString()}</td>
                                            <td className="text-end">{cat.percent}%</td>
                                            <td className="text-end">
                                                <Button variant="link" className="p-0">
                                                    <i className="bi bi-chevron-right"></i>
                                                </Button>
                                            </td>
                                        </tr>
                                    )) : (
                                        <tr className="table-light">
                                            <td colSpan={5} className="text-center text-muted">
                                                No categories
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
            <footer className="text-center text-muted small">
                © {new Date().getFullYear()} CoinKeeper
            </footer>
        </div>
    );
}

