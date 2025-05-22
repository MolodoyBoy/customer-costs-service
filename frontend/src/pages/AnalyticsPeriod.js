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
import { ApiClient, PeriodCostsAnalytics, PeriodCostsAnalyticsApi } from 'ccs-openapi-client';
import {getAuthToken} from "../auth";

ChartJS.register(
    LineElement,
    CategoryScale,
    LinearScale,
    PointElement,
    Tooltip,
    Legend
);

export default function AnalyticsPage() {
    const [periods, setPeriods] = useState([]);
    const [currentIdx, setCurrentIdx] = useState(0);
    const [currentLabel, setCurrentLabel] = useState('');
    const [chartData, setChartData] = useState({ labels: [], datasets: [] });
    const [summary, setSummary] = useState({ amount: 0, difference: 0 });

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

    useEffect(() => {
        if (!periods.length) return;
        const { period, periodCostsAnalyticId } = periods[currentIdx];
        const dt = new Date(period);
        const month = dt.toLocaleString('default', { month: 'long' });
        const year = dt.getFullYear();
        setCurrentLabel(`${month} ${year}`);

        new PeriodCostsAnalyticsApi().getPeriodCostsAnalytics(
            periodCostsAnalyticId,
            { limit: 5 },
            (err, resp) => {
                if (err) return;
                const { customerCosts, periodCostsAnalytics } = resp;
                // Chart
                const labels = customerCosts.map(c =>
                    new Date(c.createdAt).toLocaleDateString()
                );
                const dataPoints = customerCosts.map(c => c.amount);
                setChartData({
                    labels,
                    datasets: [
                        {
                            label: 'Daily Expenses',
                            data: dataPoints,
                            fill: false,
                            tension: 0.4,
                            borderWidth: 3
                        }
                    ]
                });
                // Summary
                setSummary({
                    amount: periodCostsAnalytics.amount,
                    average: periodCostsAnalytics.average,
                    difference: periodCostsAnalytics.differenceFromPrevious
                });
            }
        );
    }, [periods, currentIdx]);

    const prevPeriod = () => setCurrentIdx(i => Math.max(0, i - 1));
    const nextPeriod = () => setCurrentIdx(i => Math.min(periods.length - 1, i + 1));

    const options = {
        scales: {
            y: {
                ticks: { callback: v => `${(v / 1000).toFixed(1)}K` },
                title: { display: true, text: 'Expenses (thousands)' }
            },
            x: { title: { display: true, text: 'Day of Month' } }
        },
        plugins: {
            legend: { display: false },
            tooltip: {
                callbacks: {
                    label: ctx => `₴${Number(ctx.parsed.y).toLocaleString()}`
                }
            }
        },
        maintainAspectRatio: false
    };

    const diffColor = summary.difference > 0 ? 'red' : 'green';
    const diffValue = Math.abs(summary.difference).toLocaleString();
    const amtValue = Math.abs(summary.amount).toLocaleString();
    const average = Math.abs(summary.average).toLocaleString();
    const amtSign = summary.amount >= 0 ? '₴' : '−₴';

    return (
        <Container fluid className="py-4">
            {/* Title + Period Switcher */}
            <Row className="mb-4">
                <Col>
                    <div className="d-flex align-items-center justify-content-between">
                        <h2 className="m-0">
                            <i className="bi bi-bar-chart-line-fill me-2"></i>Analytics
                        </h2>
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
                            <h5 style={{ color: diffColor, margin: 0 }}>{`${diffValue}₴`}</h5>
                        </div>
                        <div className="mt-4 d-flex justify-content-between align-items-center">
                            <h5 className="text-muted mb-0 me-2">Average spending:</h5>
                            <h5 className="text-primary fw-semibold">{`${average}₴`}</h5>
                        </div>
                    </Card>
                </Col>
                <Col lg={8}>
                    <Card className="p-3 h-100 shadow-sm">
                        <div className="position-relative" style={{ height: '300px' }}>
                            <Line data={chartData} options={options} />
                        </div>
                    </Card>
                </Col>
            </Row>

            {/* Expense Categories */}
            <Row>
                <Col md={6} className="mb-4">
                    <Card className="shadow-sm">
                        <Card.Header as="h5" className="d-flex justify-content-between align-items-center">
                            Expense Categories
                            <Button variant="success">More ➔</Button>
                        </Card.Header>
                        <Card.Body className="p-0">
                            <Table hover className="mb-0">
                                <thead className="table-light">
                                <tr>
                                    <th>Category</th>
                                    <th>Transactions</th>
                                    <th>Amount</th>
                                </tr>
                                </thead>
                                <tbody>
                                {[
                                    { name: 'Transfers', tx: 4, amount: -21105 },
                                    { name: 'Digital Goods', tx: 5, amount: -7679 },
                                    { name: 'Online Stores', tx: 1, amount: -7177 },
                                    { name: 'Taxi', tx: 37, amount: -5554 }
                                ].map((row, i) => (
                                    <tr key={i}>
                                        <td>{row.name}</td>
                                        <td>{row.tx}</td>
                                        <td className="text-end">₴{Math.abs(row.amount).toLocaleString()}</td>
                                    </tr>
                                ))}
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
    );
}
