const chai = require("chai");
const should = chai.should();
const expect = chai.expect;
const app = require('../app')

describe('User info', async () => {
    let authHeaders;
    before(async () => {
        authHeaders = await app.authHeaders();
    });

    it("should get details", done => {
        app.authApi
            .get("/user-info")
            .set(authHeaders)
            .end((err, res) => {
                if (err) done(err)
                res.should.have.status(200);
                res.body.should.be.a("object");
                res.body.should.have.property("username");
                expect(res.body.username).to.equal('user1@test.com');
                res.body.should.have.property("address");
                expect(res.body.address).to.equal('');
                res.body.should.have.property("claims");
                expect(res.body.claims).to.have.members(['CUSTOMER']);
                done();
            });
    });

    it("should enforce password policy", done => {
        app.authApi
            .post("/user-info")
            .send({password: 'weakPassword', address: 'Another address'})
            .set(authHeaders)
            .end((err, res) => {
                if (err) done(err)
                res.should.have.status(400);
                done();
            });
    });

    it("should update details", done => {
        app.authApi
            .post("/user-info")
            .send({password: 'Password1!', address: 'My new address'})
            .set(authHeaders)
            .end((err, res) => {
                if (err) done(err)
                res.should.have.status(200);
                res.body.should.be.a("object");
                res.body.should.have.property("username");
                expect(res.body.username).to.equal('user1@test.com');
                res.body.should.have.property("address");
                expect(res.body.address).to.equal('My new address');
                res.body.should.have.property("claims");
                expect(res.body.claims).to.have.members(['CUSTOMER']);
                done();
            });
    });

    it("should get updated details", done => {
        app.authApi
            .get("/user-info")
            .set(authHeaders)
            .end((err, res) => {
                if (err) done(err)
                res.should.have.status(200);
                res.body.should.be.a("object");
                res.body.should.have.property("username");
                expect(res.body.username).to.equal('user1@test.com');
                res.body.should.have.property("address");
                expect(res.body.address).to.equal('My new address');
                res.body.should.have.property("claims");
                expect(res.body.claims).to.have.members(['CUSTOMER']);
                done();
            });
    });

});
