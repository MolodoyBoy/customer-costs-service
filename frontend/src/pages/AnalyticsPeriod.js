// src/pages/AnalyticsPage.jsx
import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Container, Row, Col, Card, Table, Button } from 'react-bootstrap';
import { Line } from 'react-chartjs-2';
import {
    Chart as ChartJS,
    LineElement,
    CategoryScale,
    LinearScale,
    PointElement,
    Tooltip,
    Legend
} from 'chart.js';
import { ApiClient, PeriodCostsAnalyticsApi } from 'ccs-openapi-client';
import {getAuthToken, logout} from '../auth';
import { Link, useNavigate } from 'react-router-dom';
import './AnalyticsPage.css';

ChartJS.register(
    LineElement,
    CategoryScale,
    LinearScale,
    PointElement,
    Tooltip,
    Legend
);

export default function AnalyticsPage() {
    const navigate = useNavigate();
    const [periods, setPeriods]           = useState([]);
    const [currentIdx, setCurrentIdx]     = useState(0);
    const [currentLabel, setCurrentLabel] = useState('');
    const [chartData, setChartData]       = useState({ labels: [], datasets: [] });
    const [summary, setSummary]           = useState({ amount: 0, average: 0, difference: 0 });
    const [categories, setCategories]     = useState([]); // New

    // load periods
    useEffect(() => {
        const token = getAuthToken();
        if (token) {
            ApiClient.instance.defaultHeaders = {
                ...ApiClient.instance.defaultHeaders,
                Authorization: token,
            };
        }
        new PeriodCostsAnalyticsApi().getAnalyticsPeriods((err, data) => {
            if (!err && Array.isArray(data.values) && data.values.length) {
                setPeriods(data.values);
                setCurrentIdx(data.values.length - 1);
            }
        });
    }, []);

    // load analytics for selected period
    useEffect(() => {
        if (!periods.length) return;
        const { period, periodCostsAnalyticId } = periods[currentIdx];
        const dt    = new Date(period);
        const month = dt.toLocaleString('default', { month: 'long' });
        const year  = dt.getFullYear();
        setCurrentLabel(`${month} ${year}`);

        new PeriodCostsAnalyticsApi().getPeriodCostsAnalytics(
            periodCostsAnalyticId,
            { limit: 5 },
            (err, resp) => {
                if (err) return;
                const { customerCosts, periodCostsAnalytics, categorizedCostsAnalytics } = resp;

                // chart
                const labels    = customerCosts.map(c => new Date(c.createdAt).toLocaleDateString());
                const dataPts   = customerCosts.map(c => c.amount);
                setChartData({
                    labels,
                    datasets: [{
                        label: 'Daily Expenses',
                        data: dataPts,
                        fill: false,
                        tension: 0.4,
                        borderWidth: 3
                    }]
                });

                // summary
                setSummary({
                    amount: periodCostsAnalytics.amount,
                    average: periodCostsAnalytics.average,
                    difference: periodCostsAnalytics.differenceFromPrevious
                });

                // categories
                setCategories(categorizedCostsAnalytics);
            }
        );
    }, [periods, currentIdx]);

    const prevPeriod = () => setCurrentIdx(i => Math.max(0, i - 1));
    const nextPeriod = () => setCurrentIdx(i => Math.min(periods.length - 1, i + 1));

    const options = {
        scales: {
            y: {
                ticks:    { callback: v => `${(v / 1000).toFixed(1)}K` },
                title:    { display: true, text: 'Expenses (thousands)' },
                grid:     { color: '#e9ecef' }
            },
            x: {
                title: { display: true, text: 'Day of Month' },
                grid:  { display: false }
            }
        },
        plugins: {
            legend: { display: false },
            tooltip: {
                callbacks: {
                    label: ctx => `₴${Number(ctx.parsed.y).toLocaleString()}`
                },
                backgroundColor: 'rgba(0,0,0,0.7)',
                titleFont:       { size: 14 },
                bodyFont:        { size: 12 },
                cornerRadius:     4,
                padding:          8
            }
        },
        maintainAspectRatio: false
    };

    const diffColor = summary.difference > 0 ? 'red' : 'green';
    const diffValue = Math.abs(summary.difference).toLocaleString();
    const amtValue = Math.abs(summary.amount).toLocaleString();
    const average = Math.abs(summary.average).toLocaleString();
    const amtSign = summary.amount >= 0 ? '₴' : '−₴';

    const handleLogout = () => {
        logout();
        navigate('/login');
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


            <Container fluid className="py-4">
                {/* Title + Period Switcher */}
                <Row className="mb-4">
                    <Col>
                        <div className="d-flex align-items-center justify-content-between">
                            <h2 className="m-0">
                                <i className="bi bi-bar-chart-line-fill me-2"></i>Analytics
                            </h2>
                            {periods.length > 1 && (
                                <div>
                                    <Button variant="light" size="sm" onClick={prevPeriod} disabled={currentIdx === 0}>
                                        &lt;
                                    </Button>
                                    <Button variant="outline-secondary" size="sm" className="mx-2" disabled>
                                        {currentLabel}
                                    </Button>
                                    <Button
                                        variant="light"
                                        size="sm"
                                        onClick={nextPeriod}
                                        disabled={currentIdx === periods.length - 1}
                                    >
                                        &gt;
                                    </Button>
                                </div>
                            )}
                        </div>
                    </Col>
                </Row>

                {/* Summary + Chart */}
                <Row className="mb-5">
                    <Col lg={4}>
                        <Card className="p-3 h-100 shadow-sm text-center">
                            <h5 className="text-muted">Expenses in {currentLabel}</h5>
                            <h2 className="my-3">{`${amtSign}${amtValue}`}</h2>
                            <div className="mt-4 d-flex justify-content-between align-items-center">
                                <h5 className="text-muted mb-0 me-2">Less than previous:</h5>
                                <h5 style={{color: diffColor, margin: 0}}>{`${diffValue}₴`}</h5>
                            </div>
                            <div className="mt-4 d-flex justify-content-between align-items-center">
                                <h5 className="text-muted mb-0 me-2">Average expenses:</h5>
                                <h5 className="text-primary fw-semibold">{`${average}₴`}</h5>
                            </div>
                        </Card>
                    </Col>
                    <Col lg={8}>
                        <Card className="p-3 h-100 shadow-sm border-0 rounded-3">
                            <Card.Body className="p-0">
                                <div className="position-relative" style={{ height: '300px' }}>
                                    <Line
                                        data={chartData}
                                        options={{
                                            ...options,
                                            elements: {
                                                line: {
                                                    tension: 0.3,
                                                    borderColor: '#6f42c1',
                                                    borderWidth: 4,
                                                    fill: 'start',
                                                    backgroundColor: ctx => {
                                                        const gradient = ctx.chart.ctx.createLinearGradient(0, 0, 0, 300);
                                                        gradient.addColorStop(0, 'rgba(111, 66, 193, 0.4)');
                                                        gradient.addColorStop(1, 'rgba(111, 66, 193, 0)');
                                                        return gradient;
                                                    }
                                                },
                                                point: {
                                                    radius: 5,
                                                    backgroundColor: '#6f42c1',
                                                    hoverRadius: 8,
                                                }
                                            },
                                            scales: {
                                                x: {
                                                    grid: {
                                                        display: false
                                                    },
                                                    ticks: {
                                                        color: '#495057',
                                                        font: { size: 12 }
                                                    }
                                                },
                                                y: {
                                                    grid: {
                                                        color: '#e9ecef'
                                                    },
                                                    ticks: {
                                                        color: '#495057',
                                                        font: { size: 12 },
                                                        callback: v => `${(v / 1000).toFixed(1)}K`
                                                    }
                                                }
                                            },
                                            plugins: {
                                                legend: { display: false },
                                                tooltip: {
                                                    backgroundColor: 'rgba(0,0,0,0.7)',
                                                    titleFont: { size: 14 },
                                                    bodyFont: { size: 12 },
                                                    cornerRadius: 4,
                                                    padding: 8,
                                                    callbacks: {
                                                        label: ctx => `₴${Number(ctx.parsed.y).toLocaleString()}`
                                                    }
                                                }
                                            },
                                            maintainAspectRatio: false
                                        }}
                                    />
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>

                {/* Expense Categories */}
                <Row>
                    <Col md={6} className="mb-4">
                        <Card className="shadow-sm">
                            <Card.Header as="h5" className="d-flex justify-content-between align-items-center">
                                Expense Categories
                                <Button variant="success" onClick={() => (window.location.href = '/analytics/period/all')}>More ➔</Button>
                            </Card.Header>
                            <Card.Body>
                                <Table hover striped className="mb-0">
                                    <thead className="table-light">
                                    <tr>
                                        <th>Category</th>
                                        <th>Transactions</th>
                                        <th className="text-end">Amount</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {categories.length > 0 ? (
                                        categories.map(cat => (
                                            <tr key={cat.id}>
                                                <td>{cat.categoryDescription}</td>
                                                <td>{cat.transactionsCount}</td>
                                                <td className="text-end">₴{cat.amount.toLocaleString()}</td>
                                            </tr>
                                        ))
                                    ) : (
                                        <tr>
                                            <td colSpan={3} className="text-center text-muted">
                                                No categories
                                            </td>
                                        </tr>
                                    )}
                                    </tbody>
                                </Table>
                            </Card.Body>
                        </Card>
                    </Col>


                    {/* Income Categories */}
                    <Col md={6} className="mb-4">
                        <Card className="shadow-sm">
                            <Card.Header as="h5" className="d-flex justify-content-between align-items-center">
                                Income Categories
                                <Button variant="success">More ➔</Button>
                            </Card.Header>
                            <Card.Body className="text-center py-5">
                                <div className="mb-3">
                                    <i className="bi bi-box-seam" style={{ fontSize: '3rem', color: '#ccc' }} />
                                </div>
                                <div className="text-muted">No income for this period</div>
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

