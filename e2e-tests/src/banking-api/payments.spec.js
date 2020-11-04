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
            .get("/payments")
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

    let paymentId;
    it("should create payment", done => {
        app.bankingApi
            .post("/payments")
            .send({
                amount: 200.00,
                currency: "EUR",
                giverAccountId: 1,
                beneficiaryAccountNumber: "LU770015555555555555",
                beneficiaryName: "Test (e2e) Beneficiary",
                communication: "Test (e2e) Payment",
            })
            .set(authHeaders)
            .end((err, res) => {
                if (err) done(err)
                res.should.have.status(200);
                res.body.should.be.a("object");
                res.body.should.have.property("giverAccount");
                res.body.giverAccount.should.have.property("accountNumber");
                expect(res.body.giverAccount.accountNumber).to.equal("LU510011111111111111");
                res.body.should.have.property("amount");
                expect(res.body.amount).to.equal(200);
                res.body.should.have.property("id");
                paymentId = res.body.id;
                done();
            });
    });

    it("should get one", done => {
        app.bankingApi
            .get("/payments")
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
                res.body.content[0].should.have.property("giverAccount");
                res.body.content[0].giverAccount.should.have.property("accountNumber");
                expect(res.body.content[0].giverAccount.accountNumber).to.equal("LU510011111111111111");
                res.body.content[0].should.have.property("amount");
                expect(res.body.content[0].amount).to.equal(200);
                res.body.content[0].should.not.have.property("status");
                done();
            });
    });

    it("should have new balance", done => {
        app.bankingApi
            .get("/balances/1")
            .set(authHeaders)
            .end((err, res) => {
                if (err) done(err)
                res.should.have.status(200);
                res.body.should.be.a("array");
                expect(res.body.length).to.equal(2);
                res.body[0].should.have.property("amount");
                expect(res.body[0].amount).to.equal(1000);
                res.body[0].should.have.property("account");
                res.body[0].account.should.have.property("accountNumber");
                expect(res.body[0].account.accountNumber).to.equal("LU510011111111111111");
                res.body[0].should.have.property("status");
                expect(res.body[0].status).to.equal("AVAILABLE");
                res.body[1].should.have.property("amount");
                expect(res.body[1].amount).to.equal(800);
                res.body[1].should.have.property("account");
                res.body[1].account.should.have.property("accountNumber");
                expect(res.body[1].account.accountNumber).to.equal("LU510011111111111111");
                res.body[1].should.have.property("status");
                expect(res.body[1].status).to.equal("END_OF_DAY");
                done();
            });
    });

    it("should delete one", done => {
        app.bankingApi
            .delete("/payments/" + paymentId)
            .set(authHeaders)
            .end((err, res) => {
                if (err) done(err)
                res.should.have.status(200);
                done();
            });
    });

    it("should get none", done => {
        app.bankingApi
            .get("/payments")
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

    it("should have balance back to initial value", done => {
        app.bankingApi
            .get("/balances/1")
            .set(authHeaders)
            .end((err, res) => {
                if (err) done(err)
                res.should.have.status(200);
                res.body.should.be.a("array");
                expect(res.body.length).to.equal(2);
                res.body[0].should.have.property("amount");
                expect(res.body[0].amount).to.equal(1000);
                res.body[0].should.have.property("account");
                res.body[0].account.should.have.property("accountNumber");
                expect(res.body[0].account.accountNumber).to.equal("LU510011111111111111");
                res.body[0].should.have.property("status");
                expect(res.body[0].status).to.equal("AVAILABLE");
                res.body[1].should.have.property("amount");
                expect(res.body[1].amount).to.equal(1000);
                res.body[1].should.have.property("account");
                res.body[1].account.should.have.property("accountNumber");
                expect(res.body[1].account.accountNumber).to.equal("LU510011111111111111");
                res.body[1].should.have.property("status");
                expect(res.body[1].status).to.equal("END_OF_DAY");
                done();
            });
    });

});
