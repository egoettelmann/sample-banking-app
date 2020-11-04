const chai = require("chai");
const should = chai.should();
const expect = chai.expect;
const app = require('../app')

describe("Balances without auth headers", () => {
    it("should not be accessible", done => {
        app.bankingApi
            .get("/balances")
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

describe('Balances for user 1', async () => {
    let authHeaders;
    before(async () => {
        authHeaders = await app.authHeaders('user1');
    });

    it("should get one for account 1", done => {
        app.bankingApi
            .get("/balances/1")
            .set(authHeaders)
            .end((err, res) => {
                if (err) done(err)
                res.should.have.status(200);
                res.body.should.be.a("array");
                expect(res.body.length).to.equal(1);
                res.body[0].should.have.property("account");
                res.body[0].account.should.have.property("accountNumber");
                expect(res.body[0].account.accountNumber).to.equal("LU510011111111111111");
                res.body[0].should.have.property("amount");
                expect(res.body[0].amount).to.equal(1000);
                done();
            });
    });

    it("should get one for account 2", done => {
        app.bankingApi
            .get("/balances/2")
            .set(authHeaders)
            .end((err, res) => {
                if (err) done(err)
                res.should.have.status(200);
                res.body.should.be.a("array");
                expect(res.body.length).to.equal(1);
                res.body[0].should.have.property("account");
                res.body[0].account.should.have.property("accountNumber");
                expect(res.body[0].account.accountNumber).to.equal("LU090012222222222222");
                res.body[0].should.have.property("amount");
                expect(res.body[0].amount).to.equal(0);
                done();
            });
    });

});
