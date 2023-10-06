const chai = require("chai");
const should = chai.should();
const expect = chai.expect;
const app = require('../app')

describe("Balances without auth headers", () => {
    it("should not be accessible", done => {
        app.bankingApi
            .get("/balances/LU510011111111111111/current")
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
            .get("/balances/LU510011111111111111/current")
            .set(authHeaders)
            .end((err, res) => {
                if (err) done(err)
                res.should.have.status(200);
                res.body.should.be.a("object");
                res.body.should.have.property("accountNumber");
                expect(res.body.accountNumber).to.equal("LU510011111111111111");
                res.body.should.have.property("value");
                expect(res.body.value).to.equal(1000);
                res.body.should.have.property("status");
                expect(res.body.status).to.equal("VALIDATED");
                done();
            });
    });

    it("should get one for account 2", done => {
        app.bankingApi
            .get("/balances/LU090012222222222222/current")
            .set(authHeaders)
            .end((err, res) => {
                if (err) done(err)
                res.should.have.status(200);
                res.body.should.be.a("object");
                res.body.should.have.property("accountNumber");
                expect(res.body.accountNumber).to.equal("LU090012222222222222");
                res.body.should.have.property("value");
                expect(res.body.value).to.equal(0);
                res.body.should.have.property("status");
                expect(res.body.status).to.equal("VALIDATED");
                done();
            });
    });

});
