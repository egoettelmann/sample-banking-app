const chai = require("chai");
const should = chai.should();
const expect = chai.expect;
const app = require('../app')

describe("Payments without auth headers", () => {
    it("should not be accessible", done => {
        app.bankingApi
            .get("/payments")
            .end((err, res) => {
                if (err) done(err)
                res.should.have.status(401);
                res.body.should.be.a("object");
                res.body.should.have.property("title");
                res.body.should.have.property("detail");
                done();
            });
    });
});

describe('Payments for user 1', async () => {
    let authHeaders;
    before(async () => {
        authHeaders = await app.authHeaders('user1');
    });

    it("should get none", done => {
        app.bankingApi
            .get("/payments?originAccountNumber=LU510011111111111111")
            .set(authHeaders)
            .end((err, res) => {
                if (err) done(err)
                res.should.have.status(200);
                res.body.should.be.a("object");
                res.body.should.have.property("totalElements");
                expect(res.body.totalElements).to.equal(0);
                res.body.should.have.property("content");
                res.body.content.should.be.a("array");
                expect(res.body.content.length).to.equal(0);
                done();
            });
    });

    it("should create payment", done => {
        app.bankingApi
            .post("/payments")
            .send({
                amount: 200.00,
                currency: "EUR",
                originAccountNumber: "LU510011111111111111",
                beneficiaryAccountNumber: "LU770015555555555555",
                beneficiaryName: "Test (e2e) Beneficiary",
                communication: "Test (e2e) Payment",
            })
            .set(authHeaders)
            .end((err, res) => {
                if (err) done(err)
                res.should.have.status(200);
                res.body.should.be.a("object");
                res.body.should.have.property("originAccountNumber");
                expect(res.body.originAccountNumber).to.equal("LU510011111111111111");
                res.body.should.have.property("amount");
                expect(res.body.amount).to.equal(200);
                res.body.should.have.property("reference");
                done();
            });
    });

    it("should get one", done => {
        app.bankingApi
            .get("/payments?originAccountNumber=LU510011111111111111")
            .set(authHeaders)
            .end((err, res) => {
                if (err) done(err)
                res.should.have.status(200);
                res.body.should.be.a("object");
                res.body.should.have.property("totalElements");
                expect(res.body.totalElements).to.equal(1);
                res.body.should.have.property("content");
                res.body.content.should.be.a("array");
                expect(res.body.content.length).to.equal(1);
                res.body.content[0].should.have.property("originAccountNumber");
                expect(res.body.content[0].originAccountNumber).to.equal("LU510011111111111111");
                res.body.content[0].should.have.property("amount");
                expect(res.body.content[0].amount).to.equal(200);
                res.body.content[0].should.have.property("status");
                expect(res.body.content[0].status).to.equal("ACCEPTED");
                done();
            });
    });

    it("should have new balance", done => {
        app.bankingApi
            .get("/balances/LU510011111111111111/current")
            .set(authHeaders)
            .end((err, res) => {
                if (err) done(err)
                res.should.have.status(200);
                res.body.should.be.a("object");
                res.body.should.have.property("value");
                expect(res.body.value).to.equal(800);
                res.body.should.have.property("accountNumber");
                expect(res.body.accountNumber).to.equal("LU510011111111111111");
                res.body.should.have.property("status");
                expect(res.body.status).to.equal("PROVISIONAL");
                done();
            });
    });

});
